package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

/**
 * test
 * @author Jeezy
 */
public class Main extends SimpleApplication implements AnimEventListener {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    protected Spatial teapot, teapot2, ninja;
    protected Material pulsingBoxMat, changingMat, sphereMat;
    protected Geometry pulsingBox, colorChangingBox, rollingBox, sphereGeo;
    protected Sphere sphereMesh;
    
    private AnimChannel animChannel1Oto, animChannel2Oto;
    private AnimControl animControlOto;
    Node oto;
    
    Boolean isRunning=true;
    Boolean isFlyCamEnabled=true;
    Boolean isSphereShining=true;
 
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
 
        /*assetManager.registerLocator("assets/town.zip", ZipLocator.class); //adds zip file to 'library'
        Spatial gameLevel = assetManager.loadModel("main.scene"); //scene inside of zip file*/
        
        Spatial gameLevel = assetManager.loadModel("Scenes/town/main.scene");
        //Spatial gameLevel = assetManager.loadModel("Scenes/town/main.j3o"); //same as above, export main.scene to binary and comment line above
        
        gameLevel.setLocalTranslation(0, -5.2f, 0);
        gameLevel.setLocalScale(2);
        rootNode.attachChild(gameLevel);
    
        //Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
        teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
        Material mat_default = new Material( 
            assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        rootNode.attachChild(teapot);
        
        teapot2 = assetManager.loadModel("Models/Teapot/Teapot.obj");
        teapot2.setMaterial(mat_default); //use same material for the second teapot
        teapot2.setLocalTranslation(2.0f, 0.0f, 0.0f);
        rootNode.attachChild(teapot2);
 
        // Create a wall with a simple texture from test_data
        Box box = new Box(2.5f,2.5f,1.0f);
        Spatial wall = new Geometry("Box", box );
        Material mat_brick = new Material( 
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_brick.setTexture("ColorMap", 
            assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        wall.setMaterial(mat_brick);
        wall.setLocalTranslation(2.0f,-2.5f,0.0f);
        rootNode.attachChild(wall);
 
        // Display a line of text with a default font
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Hello Practice");
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
 
        // Load a model from test_data (OgreXML + material + texture)
        ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja.scale(0.05f, 0.05f, 0.05f);
        ninja.rotate(0.0f, -3.0f, 0.0f);
        ninja.setLocalTranslation(0.0f, -5.0f, -2.0f);
        rootNode.attachChild(ninja);
        
        // add a pulsing box to the scene
        Box a = new Box(0.5f, 0.5f, 0.5f);
        pulsingBox = new Geometry("pulsing box", a);
        pulsingBoxMat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");
        pulsingBoxMat.setColor("Color", ColorRGBA.Brown);
        pulsingBox.setMaterial(pulsingBoxMat);
        pulsingBox.setLocalTranslation(-6.0f, -2.0f, 6.0f);
        rootNode.attachChild(pulsingBox);
        
        // add a color changing box to the scene
        Box b = new Box(0.5f, 0.5f, 0.5f);
        colorChangingBox = new Geometry("color changing box", b);
        changingMat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");
        changingMat.setColor("Color", ColorRGBA.Red);
        colorChangingBox.setMaterial(changingMat);
        colorChangingBox.setLocalTranslation(4.0f, 0.5f, 0.0f);
        rootNode.attachChild(colorChangingBox);
        
        // add a rolling textured box to the scene
        Box c = new Box(1, 1, 1);
        rollingBox = new Geometry("rolling box", c);
        Material cMat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");
        Texture cTex = assetManager.loadTexture(
            "Textures/Us.jpg");
        cMat.setTexture("ColorMap", cTex);
        rollingBox.setMaterial(cMat);
        rollingBox.setLocalTranslation(-3.0f, -3.5f, 0.0f);
        rootNode.attachChild(rollingBox);
        
        //create sphere with bumpy, shiny texture
        sphereMesh = new Sphere(32,32, 2f);
        sphereGeo = new Geometry("Shiny rock", sphereMesh);
        sphereMesh.setTextureMode(Sphere.TextureMode.Projected); //for use with spheres only
        TangentBinormalGenerator.generate(sphereMesh);
        
        //create and set bumpy, shiny material for the sphere
        /*sphereMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
        sphereMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg")); //standard rocky texture
        sphereMat.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png")); //set bumpiness map
        sphereMat.setBoolean("UseMaterialColors",true);    
        sphereMat.setColor("Diffuse",ColorRGBA.White);  // minimum material color
        sphereMat.setColor("Specular",ColorRGBA.White); // for shininess
        sphereMat.setFloat("Shininess", 64f); // [1,128] for shininess
        sphereGeo.setMaterial(sphereMat);*/
        
        //faster way to set bumpy, shiny material for the sphere using .j3m file
        sphereMat = (Material) assetManager.loadMaterial("Materials/MyCustomMaterial.j3m");
        sphereGeo.setMaterial(sphereMat);
        
        sphereGeo.setLocalTranslation(6,2,6); // Move it a bit
        sphereGeo.rotate(1.6f, 0, 0);         // Rotate it a bit
        rootNode.attachChild(sphereGeo);
        
        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);
        
        initKeys(); // load my custom keybinding
        
        /*Oto*/
        oto = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml"); //load model
        oto.setLocalScale(0.5f); //shrink model
        oto.setLocalTranslation(-10, 0, -25);
        rootNode.attachChild(oto); //attach to root node to display in scene
        animControlOto = oto.getControl(AnimControl.class); //load animation controls
        animControlOto.addListener(this); //listen to animation events
        animChannel1Oto = animControlOto.createChannel(); //create animation channel, can use two channels for upper and lower body!
        animChannel2Oto = animControlOto.createChannel(); //create second channel for multiple animations simultaneously
        animChannel1Oto.setAnim("stand"); //set default animation sequence
        animChannel2Oto.setAnim("stand"); //set default animation sequence
        /*display list of available animation sequences for controller*/
        for (String anim : animControlOto.getAnimationNames()) { System.out.println(anim); } //display list of available animation sequences for controller
    }
    
    /** Custom Keybinding: Map named actions to inputs. */
    private void initKeys() {
      // You can map one or several inputs to one named action
      inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
      inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_J));
      inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_L));
      inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_I));
      inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_K));
      /*inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
                                        new MouseButtonTrigger(MouseInput.BUTTON_LEFT));*/
      inputManager.addMapping("TeapotUp", new KeyTrigger(KeyInput.KEY_NUMPAD8));
      inputManager.addMapping("TeapotDown", new KeyTrigger(KeyInput.KEY_NUMPAD2));
      inputManager.addMapping("ToggleFlyCam", new KeyTrigger(KeyInput.KEY_CAPITAL));
      inputManager.addMapping("ToggleSphereShining", new KeyTrigger(KeyInput.KEY_NUMLOCK));
      
      // Add the names to the combines action/analog listener.
      inputManager.addListener(combinedListener, new String[]{"ToggleFlyCam", "Pause", "Left", "Right", "Forward", "Backward"});
      inputManager.addListener(combinedListener, new String[]{"TeapotUp", "TeapotDown"});
      inputManager.addListener(combinedListener, new String[]{"ToggleSphereShining"});
      
      //map key to "Walk" and add to listener (this is for Oto only)
      inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_SPACE));
      inputManager.addMapping("Dodge", new KeyTrigger(KeyInput.KEY_V));
      inputManager.addMapping("push", new KeyTrigger(KeyInput.KEY_B));
      inputManager.addListener(combinedListener, new String[]{"Walk", "Dodge", "push"});
    }
    
    //add listener for keystrokes/mouse input
    private MyCombinedListener combinedListener = new MyCombinedListener();
    private class MyCombinedListener implements AnalogListener, ActionListener {
      
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("Pause") && !keyPressed) {
            isRunning = !isRunning;
          }
          if (name.equals("ToggleFlyCam") && !keyPressed) {
            isFlyCamEnabled = !isFlyCamEnabled;
            flyCam.setEnabled(isFlyCamEnabled);
          }
          if (name.equals("ToggleSphereShining") && !keyPressed) {
            isSphereShining = !isSphereShining;
            if(isSphereShining) {
                sphereMat.setColor("Specular",ColorRGBA.White); // for shininess
            } else {
                sphereMat.setColor("Specular",ColorRGBA.Black); // for shininess
            }
          }
        }

        public void onAnalog(String name, float value, float tpf) {
          if (isRunning) {
            if (name.equals("Left")) {
              ninja.rotate(0, value*speed*2, 0);
            }
            if (name.equals("Right")) {
              ninja.rotate(0, -value*speed*2, 0);
            }
            if (name.equals("Forward")) {
              Vector3f v = ninja.getLocalTranslation();
              ninja.setLocalTranslation(v.x + value*speed*2, v.y, v.z);
            }
            if (name.equals("Backward")) {
              Vector3f v = ninja.getLocalTranslation();
              ninja.setLocalTranslation(v.x - value*speed*2, v.y, v.z);
            }
            if (name.equals("TeapotUp")) {
              Vector3f v = teapot.getLocalTranslation();
              teapot.setLocalTranslation(v.x, v.y + value*speed*2, v.z);
            }
            if (name.equals("TeapotDown")) {
              Vector3f v = teapot.getLocalTranslation();
              teapot.setLocalTranslation(v.x, v.y - value*speed*2, v.z);
            }
            if (name.equals("Walk")) {
                if (!animChannel1Oto.getAnimationName().equals("Walk")) {
                  animChannel1Oto.setAnim("Walk", 0.50f);
                  animChannel1Oto.setLoopMode(LoopMode.Loop);
                }
                
                Vector3f v = oto.getLocalTranslation();
                oto.setLocalTranslation(v.x, v.y, v.z + value*speed*6);
              }
              if (name.equals("Dodge")) {
                if (!animChannel2Oto.getAnimationName().equals("Dodge")) {
                  animChannel2Oto.setAnim("Dodge", 0.50f);
                  animChannel2Oto.setLoopMode(LoopMode.Loop);
                }
              }
              if (name.equals("push")) {
                if (!animChannel2Oto.getAnimationName().equals("push")) {
                  animChannel2Oto.setAnim("push", 0.50f);
                  animChannel2Oto.setLoopMode(LoopMode.Loop);
                }
              }
          } else {
            //System.out.println("Press P to unpause.");
          }
        }
    }
    
    /* Use the main event loop to trigger repeating actions. */
    @Override
    public void simpleUpdate(float tpf) {
        if(isRunning) {
            // make the teapots rotate:
            teapot.rotate(0, tpf, 0);
            teapot2.rotate(0, -4*tpf, 0);

            long currTimeMillis = System.currentTimeMillis();

            //make box 'a' pulsate
            long millis = currTimeMillis % 1000;
            if(millis < 150) {
                pulsingBox.setLocalScale(1.2f, 1.2f, 1.2f);
            } else {
                pulsingBox.setLocalScale(1.0f, 1.0f, 1.0f);
            }

            //change the color changing material
            long timeInSecs = currTimeMillis / 1000;
            int secs = (int)(timeInSecs % 60); //get second counter
            if(secs < 10) {
                changingMat.setColor("Color", ColorRGBA.Red); //this material is linked to Box b
            } else if(secs < 20) {
                changingMat.setColor("Color", ColorRGBA.Green); //this material is linked to Box b
            } else if(secs < 30) {
                changingMat.setColor("Color", ColorRGBA.Blue); //this material is linked to Box b
            } else if(secs < 40) {
                changingMat.setColor("Color", ColorRGBA.Yellow); //this material is linked to Box b
            } else if(secs < 50) {
                changingMat.setColor("Color", ColorRGBA.Orange); //this material is linked to Box b
            } else {
                changingMat.setColor("Color", ColorRGBA.White); //this material is linked to Box b
            }

            //rotate box along x-axis and push along z-axis
            rollingBox.rotate(-tpf, 0, 0);
            rollingBox.move(0, 0, -tpf);
            
            //rotate the sphere
            sphereGeo.rotate(0.0f, 0.0f, tpf/2);
        }
    }

    /*animation event handlers*/
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
      // unused
    }
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Walk")) {
          channel.setAnim("stand", 0.50f);
          channel.setLoopMode(LoopMode.DontLoop);
          channel.setSpeed(1f);
        }
        if (animName.equals("Dodge")) {
          channel.setAnim("stand", 0.50f);
          channel.setLoopMode(LoopMode.DontLoop);
          channel.setSpeed(1f);
        }
        if (animName.equals("push")) {
          channel.setAnim("stand", 0.50f);
          channel.setLoopMode(LoopMode.DontLoop);
          channel.setSpeed(1f);
        }
    }
}