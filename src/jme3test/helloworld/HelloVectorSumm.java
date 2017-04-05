//http://wiki.jmonkeyengine.org/doku.php/jme3:beginner:hellovector

package jme3test.helloworld;
 
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import org.jmonkey.utils.Debug;
import org.jmonkey.utils.MaterialUtils;
import org.jmonkey.utils.SpatialUtils;
 
/**
 Example Vector Summ
 
 @author Alex Cham aka Jcrypto
 */
public class HelloVectorSumm extends SimpleApplication
{
 
    private Node vctrNode = SpatialUtils.makeNode("vectorNode");
    //
    private Vector3f vctrNodeLoc = new Vector3f(64.0f, 64.0f, 64.0f);
    private Vector3f camLocVctr = new Vector3f(512.0f, 64.0f, 0.0f);
    //
    private Vector3f vctrNodeSpatLoc = new Vector3f(64.0f, 128.0f, -32.0f);
    //
    private Vector3f vctrSumm = null;
    private Vector3f scale = new Vector3f(8, 8, 8);
 
    public static void main(String[] args)
    {
        HelloVectorSumm app = new HelloVectorSumm();
        //app.setShowSettings(false);
        app.start();
    }
 
    @Override
    public void simpleInitApp()
    {
        cam.setLocation(camLocVctr);
        cam.lookAt(Vector3f.ZERO, cam.getUp());
        flyCam.setMoveSpeed(100.0f);
        //
        Debug.showNodeAxes(assetManager, rootNode, 128);
        Debug.attachWireFrameDebugGrid(assetManager, rootNode, Vector3f.ZERO, 256, ColorRGBA.DarkGray);
 
        //     
        Box box = new Box(1, 1, 1);
        //
        Material mat = MaterialUtils.makeMaterial(assetManager, "Common/MatDefs/Misc/Unshaded.j3md", ColorRGBA.Blue);
        Geometry geom = SpatialUtils.makeGeometry(vctrNodeSpatLoc, scale, box, mat, "box");
        vctrNode.attachChild(geom);
        vctrNode.setLocalTranslation(vctrNodeLoc);
        vctrSumm = vctrNodeLoc.add(vctrNodeSpatLoc);
        //
        Debug.showNodeAxes(assetManager, vctrNode, 4.0f);
        Debug.showVector3fArrow(assetManager, rootNode, vctrNodeLoc, ColorRGBA.Red, "vctrNodeLoc");
        Debug.showVector3fArrow(assetManager, vctrNode, vctrNodeSpatLoc, ColorRGBA.Green, "vctrNodeSpatLoc");
        Debug.showVector3fArrow(assetManager, rootNode, vctrSumm, ColorRGBA.Blue, "vctrSumm");
        //
        rootNode.attachChild(vctrNode);
    }
 
    @Override
    public void simpleUpdate(float tpf)
    {
        //n.move(tpf * 10, 0, 0);
    }
 
    @Override
    public void simpleRender(RenderManager rm)
    {
        //TODO: add render code
    }
}