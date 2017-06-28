package net.msdh.h3d;


import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import org.w3c.dom.UserDataHandler;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Enumeration;
import java.util.Hashtable;

public class Home3D extends MouseAdapter {

  protected Canvas3D canvas3D;
  protected SimpleUniverse u = null;
  protected BranchGroup scene = null;
  //protected Scene s;
  //protected float eyeOffset =0.00F;
  protected int CanvasH=600;
  protected int CanvasW=800;
  protected PickCanvas pickCanvas;
  private String texturePatch;
  private String objectPatch;
  private Hashtable objects;

  private Transform3D myTransform3DX;
  private Transform3D myTransform3DZ;
  private TransformGroup mainTrans;
  private String object;


  public Canvas3D getCanvas3D(){
    return canvas3D;
  }

  public Home3D(String objectPath, String texturePatch, int w, int h){

    this.objectPatch = objectPath;
    this.texturePatch = texturePatch;
    this.CanvasH = h;
    this.CanvasW = w;
  }

  public Home3D(){
//    canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
//    canvas3D.setSize(CanvasW, CanvasH);
//    u = new SimpleUniverse(canvas3D);
//    scene = createSceneGraph(0);
//    u.getViewingPlatform().setNominalViewingTransform();
//    u.addBranchGraph(scene);
  }

  public void destroy() {
    u.removeAllLocales();
  }

  public void create(){
    canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
    canvas3D.setSize(CanvasW,CanvasH);

    u = new SimpleUniverse(canvas3D);
    scene = createSceneGraph();
    u.getViewingPlatform().setNominalViewingTransform();
    u.addBranchGraph(scene);

    pickCanvas = new PickCanvas(canvas3D, scene);
    pickCanvas.setMode(PickCanvas.TYPE_SHAPE3D);

 //The following three lines enable navigation through the scene using the mouse.
//    OrbitBehavior ob = new OrbitBehavior(canvas3D);
//    ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE));
//    u.getViewingPlatform().setViewPlatformBehavior(ob);


    canvas3D.addMouseListener(this);
    canvas3D.addMouseMotionListener(this);
  }

  public BranchGroup createSceneGraph() {
    //objects = new HashMap<String,Shape3D>();
    BranchGroup rootGraph = new BranchGroup();
    try{
       rootGraph.setCapability(Group.ALLOW_CHILDREN_EXTEND);
       rootGraph.setCapability(Group.ALLOW_CHILDREN_READ);
       rootGraph.setCapability(Group.ALLOW_CHILDREN_WRITE);
	  myTransform3DX = new Transform3D(); //поворот по оси x
      myTransform3DX.rotX(-1.4f);
      myTransform3DZ = new Transform3D();  //поворот по оси z
      myTransform3DZ.rotZ(1.4f);

      myTransform3DX.mul(myTransform3DZ);     //слияние двух трансформаций

	  mainTrans = new TransformGroup(myTransform3DX); //создание группы трансформаций

      //objTrans.setCapability(Group.ALLOW_CHILDREN_EXTEND);
      mainTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
      mainTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

      mainTrans.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
      mainTrans.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
      mainTrans.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);


      ObjectFile f = new ObjectFile();
      f.setFlags(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
      System.out.println("Loading scene ...");

      Scene s = f.load(objectPatch+"\\"+object+".obj");
      mainTrans.addChild(s.getSceneGroup());

      objects = s.getNamedObjects();


      for(Enumeration e = objects.keys() ; e.hasMoreElements() ;){
        Object key = e.nextElement();
        System.out.println(key);

        Shape3D shape  = (Shape3D)objects.get(key);

        shape.setUserData(new DataInfo("cam", "device", "start", "off"));

        shape.setName(key.toString());

        shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        shape.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
        shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);

        shape.getAppearance().setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
        shape.getAppearance().setCapability(Appearance.ALLOW_MATERIAL_WRITE);

//        if(key.equals("vnesnie_sten_")){
//
//          Appearance ar = new Appearance();
//
//          TextureLoader loader = new TextureLoader(texturePatch+"\\"+"brick.jpg","LUMINANCE", new Container());
//          Texture texture = loader.getTexture();
//          texture.setBoundaryModeS(Texture.WRAP);
//          texture.setBoundaryModeT(Texture.WRAP);
//          texture.setBoundaryColor( new Color4f( 0.0f, 1.0f, 0.0f, 0.0f ) );
//
//          TextureAttributes texAttr = new TextureAttributes();
//          texAttr.setTextureMode(TextureAttributes.MODULATE);
//
//          ar.setTexture(texture);
//          ar.setTextureAttributes(texAttr);
//          shape.setUserData(new DataInfo("cam", "device", "start","off"));
//          ar.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
//          shape.setAppearance(ar);
//
//
//    // Add the Shadow Polygon object into the scene
//         // rootGraph.addChild(shadow);
//          //canvas3D.repaint();
//        }


      }

      System.out.println( "Finished Loading" );

      BoundingSphere bounds = new BoundingSphere(new Point3d(0.0f,0.0f,0.0f), 500.0);
     //освещение сцены
 //     rootGraph.addChild(addLight());

      PointLight staticLight = new PointLight();
      staticLight.setInfluencingBounds(bounds);
      staticLight.setAttenuation(2, 0, 0);
      staticLight.setPosition(10, 10, 0);
      staticLight.setColor(new Color3f(1f,1f,1f));
      rootGraph.addChild(staticLight);

      Color3f directionalLightColor = new Color3f(Color.BLUE);


      Color3f ambientLightColor = new Color3f(Color.WHITE);
      Vector3f lightDirection = new Vector3f(-1F, -1F, -1F);

      AmbientLight ambientLight = new AmbientLight(ambientLightColor);
      DirectionalLight directionalLight = new DirectionalLight(directionalLightColor, lightDirection);

      Bounds influenceRegion = new BoundingSphere();
      ambientLight.setInfluencingBounds(influenceRegion);

      directionalLight.setInfluencingBounds(influenceRegion);

      //objRoot.addChild(directionalLight);
      rootGraph.addChild(ambientLight);

      Background background = new Background(new Color3f(1f,1f,1f)); //задний фон белого цвета
      background.setApplicationBounds(bounds);
      rootGraph.addChild(background);

      MouseRotate mRotate = new MouseRotate(mainTrans);
      mRotate.setSchedulingBounds(bounds);
      rootGraph.addChild(mRotate);

      MouseWheelZoom mZoom = new MouseWheelZoom(mainTrans);
      mZoom.setSchedulingBounds(bounds);
      rootGraph.addChild(mZoom);

	  MouseTranslate mTranslate = new MouseTranslate(mainTrans);
      mTranslate.setSchedulingBounds(bounds);
      rootGraph.addChild(mTranslate);

	  KeyNavigatorBehavior keyNavigator = new KeyNavigatorBehavior(mainTrans);
	  keyNavigator.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
      rootGraph.addChild(keyNavigator);

//      BoundingSphere bounds1 = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
//      PickMouseBehavior pmb = new PickMouseBehavior(canvas3D,rootGraph,bounds1) {
//          @Override
//          public void updateScene(int x, int y) {
//           // pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
//            Primitive pickedShape = null;
//            pickCanvas.setShapeLocation(x,y);
//            //pickCanvas.pickAny()
//            PickResult pResult = pickCanvas.pickAny();// pickClosest();
//            if(pResult != null){
//              pickedShape = (Primitive) pResult.getNode(PickResult.PRIMITIVE);
//            //  System.out.println(pickedShape.getName());
//              if(pickedShape!=null){
//
//              Color3f color;
//              Appearance app= new Appearance();
////              if(e.getButton()==1){
////                color= new Color3f(Color.red);
////              }
////              else{
//                color= new Color3f(Color.green);
////              }
//              Color3f black= new Color3f(0.0f,0.0f,0.f);
//              Color3f white= new Color3f(1.0f,1.0f,1.0f);
//
//              app.setMaterial(new Material(color,black,color,white,70f));
//
//              pickedShape.setAppearance(app);
//
//              }
//            }
//            else{
//              System.out.println("Nothing picked");
//            }
//            System.out.println("x: " + x + " y: " + y);
//          }
//      };

//      pmb.setSchedulingBounds(bounds1);

//     rootGraph.addChild(pmb);

       // MouseBehavior mb = new MouseBehavior(mainTrans);
//        PickMouseBehavior pickMouseBehavior  =new PickMouseBehavior() {
//            @Override
//            public void updateScene(int i, int i1) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        }

      rootGraph.addChild(mainTrans);
    }
    catch(Throwable t){
      System.out.println("Error create scene: "+t);
    }
    return rootGraph;
  }


  private BranchGroup addLight(){

     BranchGroup bgLight = new BranchGroup();

     BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.MAX_VALUE);

       //Directional light.
     Color3f lightColour1 = new Color3f(1f,1f,1f);
     //Vector3f lightDir1  = new Vector3f(-1.0f,0.0f,-0.5f);
     Vector3f lightDir1  = new Vector3f(-1.0f,-1.0f,1.0f);
     DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
     light1.setInfluencingBounds(bounds);

     //bgLight.addChild(light1);

  //The transformation group for the directional light and its rotation.
    TransformGroup tfmLight = new TransformGroup();
    tfmLight.addChild(light1);

//    //The Alpha for the rotation.
//    Alpha alphaLight = new Alpha(-1,4000);
//    //The rotation
//    RotationInterpolator rot = new RotationInterpolator(alphaLight,tfmLight,new Transform3D(),0.0f,(float) Math.PI*2);
//    rot.setSchedulingBounds(bounds);
    tfmLight.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  //  tfmLight.addChild(rot);

    bgLight.addChild(tfmLight);

       //Ambient light.
    Color3f lightColourAmb = new Color3f(1f, 1f, 1f);
    AmbientLight lightAmb = new AmbientLight(lightColourAmb);
    lightAmb.setInfluencingBounds(bounds);
    bgLight.addChild(lightAmb);

    return bgLight;
  }

  public void setLightPosition(){


  }

  public void sceneReset(){
    myTransform3DX.set(new Vector3f(0f,0f,0f));
    myTransform3DZ.set(new Vector3f(0f,0f,0f));
    myTransform3DX.rotX(-1.4f);
    myTransform3DZ.rotZ(1.4f);

    myTransform3DX.mul(myTransform3DZ);
    mainTrans.setTransform(myTransform3DX);
  }



  public Hashtable getObjectsName(){
    return objects;
  }

  public void setUnvisible(String objName){
    Shape3D shape;
    try{
      shape  = (Shape3D)objects.get(objName);
      shape.setPickable(false);

      shape.getAppearance().setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLENDED,100.0f,TransparencyAttributes.BLEND_SRC_ALPHA,TransparencyAttributes.BLEND_ONE));
//      System.out.println("trcy: " + shape.getAppearance().getTransparencyAttributes().getTransparency());
//      System.out.println("trcy mode: " + shape.getAppearance().getTransparencyAttributes().getTransparencyMode());
//      System.out.println("srcfBlend: " + shape.getAppearance().getTransparencyAttributes().getSrcBlendFunction());
//      System.out.println("dstfBlend: " + shape.getAppearance().getTransparencyAttributes().getDstBlendFunction());
      System.out.println("set unvisible");
    }
    catch(Throwable t){
      System.out.println("Error: "+t);
    }
  }


  public void setVisible(String objName){
    Shape3D shape;
    try{
      shape = (Shape3D)objects.get(objName);
      System.out.println("shape iz object vibrano");

      shape.setPickable(true);
      shape.getAppearance().setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NONE,0.0f,TransparencyAttributes.BLEND_SRC_ALPHA,TransparencyAttributes.BLEND_ONE));
      System.out.println("set visible");
    }
    catch(Throwable t){
      System.out.println("Error: "+t);
    }
  }

  public void selectObject(String objName){
    Color3f color= new Color3f(Color.green);
    Color3f black= new Color3f(0.0f,0.0f,0.f);
    Color3f white= new Color3f(1.0f,1.0f,1.0f);
    Shape3D shape;
    try{
      shape = (Shape3D)objects.get(objName);
      shape.getAppearance().setMaterial(new Material(color, black, color, white, 70f));
      System.out.println("set visible");
    }
    catch(Throwable t){
      System.out.println("Error: "+t);
    }
  }

    public void selectObject(Shape3D shape){
       Color3f color= new Color3f(Color.green);
       Color3f black= new Color3f(0.0f,0.0f,0.f);
       Color3f white= new Color3f(1.0f,1.0f,1.0f);

       try{
         shape.getAppearance().setMaterial(new Material(color,black,color,white,70f));
         System.out.println("set visible");
       }
       catch(Throwable t){
         System.out.println("Error: "+t);
       }
    }


  public void unselectObject(Shape3D shape){
    try{
      Color3f white= new Color3f(1.0f,1.0f,1.0f);
      shape.getAppearance().setMaterial(new Material(white,white,white,white,70f));
      System.out.println("set visible");
    }
    catch(Throwable t){
      System.out.println("Error: "+t);
    }
  }

  public void setStat(String objName, String stat){
    Shape3D shape;
    try{
      shape = (Shape3D)objects.get(objName);
      DataInfo di = (DataInfo)shape.getUserData();
      di.stat = stat;

    }
    catch(Throwable t){
      System.out.println("Error: "+t);
    }
  }

  public void setStat(Shape3D shape, String stat){
    try{
      DataInfo di = (DataInfo)shape.getUserData();
      di.stat = stat;
    }
    catch(Throwable t){
      System.out.println("Error: "+t);
    }
  }


  public void deleteShape(String objName){
    try{
      Shape3D shape = (Shape3D)objects.get(objName);
      shape.removeAllGeometries();
      objects.remove(objName);
    }
    catch(Throwable t){
      System.out.println("Error: "+t);
    }
  }


  public void loadShape(String objName){

    Shape3D shape = null;
    Scene tS = null;

    ObjectFile f = new ObjectFile();
    f.setFlags(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
    try{

      tS = f.load(objectPatch+"\\"+objName+".obj");
      shape = (Shape3D)tS.getSceneGroup().getChild(1);

      shape.setName(objName);

      shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
      shape.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
      shape.getAppearance().setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
      Transform3D tr = new Transform3D( );
      tr.setScale(0.5);
      TransformGroup objTrans1 = new TransformGroup();
      objTrans1.setTransform(tr);

      BranchGroup bg = new BranchGroup();
      tS.getSceneGroup().removeAllChildren();
      bg.addChild(shape);
      objTrans1.addChild(bg);

      BranchGroup bg2 = new BranchGroup();
      bg2.addChild(objTrans1);

      scene.addChild(bg2);

      if(!objects.containsKey(objName)){
        objects.put(objName,shape);
      }
    }
    catch(Exception e){
      System.out.println("Error add shape: " + e.getMessage());
    }

//    //create the image to be rendered using a Raster
//BufferedImage bufferedImage =
// new BufferedImage( 128, 128, BufferedImage.TYPE_INT_RGB);
//
////load or do something to the image here…
////wrap the BufferedImage in an ImageComponent2D
//ImageComponent2D imageComponent2D =
// new ImageComponent2D( ImageComponent2D.FORMAT_RGB,
//                       bufferedImage);
//imageComponent2D.setCapability( ImageComponent.ALLOW_IMAGE_READ );
//imageComponent2D.setCapability( ImageComponent.ALLOW_SIZE_READ );
//
////create the Raster for the image
//m_RenderRaster = new Raster(  new Point3f( 0.0f, 0.0f, 0.0f ),
//                              Raster.RASTER_COLOR,
//                              0, 0,
//                              bufferedImage.getWidth(),
//                              bufferedImage.getHeight(),
//                              imageComponent2D,
//                              null );
//m_RenderRaster.setCapability( Raster.ALLOW_IMAGE_WRITE );
//m_RenderRaster.setCapability( Raster.ALLOW_SIZE_READ );
//
////wrap the Raster in a Shape3D
//Shape3D shape = new Shape3D( m_RenderRaster );
  }


    public void mouseClicked(MouseEvent e){

      pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
      pickCanvas.setShapeLocation(e);

      pickCanvas.setFlags(PickInfo.ALL_GEOM_INFO|PickInfo.NODE);

      PickInfo result = pickCanvas.pickClosest();

      if(result != null){

        System.out.println("button: " + e.getButton());
       // System.out.println(result.getNode().getClass().getName());
        if(result.getNode() instanceof Shape3D){

//          Color3f color;
//          Appearance app= new Appearance();
//          if(e.getButton()==1){
//            color= new Color3f(Color.red);
//          }
//          else{
//            color= new Color3f(Color.green);
//          }
//          Color3f black= new Color3f(0.0f,0.0f,0.f);
//
//          Color3f white= new Color3f(1.0f,1.0f,1.0f);
//          app.setMaterial(new Material(color,black,color,white,70f));
//
//          Shape3D temps = (Shape3D) result.getNode();
            if(e.getButton()==1){
              selectObject((Shape3D) result.getNode());
            } else{
              unselectObject((Shape3D) result.getNode());

            }

          //temps.setUserData(new DataInfo("cam","device","start"));

         // System.out.println("select shape: " + temps.getName());
          //temps.setAppearance(app);
         // System.out.println(((DataInfo)temps.getUserData()).type);

//          try {
//            Connection cnn = new Connection();
//            cnn.Send("127.0.0.1",60000,"test dev name: " + temps.getName()+" action: " + (e.getButton()==1?"on":"off"));
//            cnn.CloseClient();
//          }
//          catch (IOException e1) {
//                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//          }

//        canvas3D.repaint();
        }
      }
      else{
        System.out.println("Nothing picked");

         myTransform3DX.set(new Vector3f(0f,0f,0f));
         myTransform3DZ.set(new Vector3f(0f,0f,0f));
         myTransform3DX.rotX(-1.4f);
         myTransform3DZ.rotZ(1.4f);

//        Transform3D current = new Transform3D();
//        objTrans.getTransform(current);
//
//        Vector3f v = new Vector3f();
//
//        current.get(v);
//
//        System.out.println("X: "+ v.getX()+" Y: " + v.getY()+" Z: " + v.getZ());

         myTransform3DX.mul(myTransform3DZ);
         mainTrans.setTransform(myTransform3DX);

//        double x =  mRotate.getXFactor();
//        System.out.println("X: " + x);
      }
    }


    public void mouseEntered(MouseEvent e) {
//      System.out.println("mouseEntered");
//
//      System.out.println("mouseEntered object: " + e.getSource().getClass().getName());
//
//      pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
//      pickCanvas.setShapeLocation(e);
//
//      pickCanvas.setFlags(PickInfo.ALL_GEOM_INFO|PickInfo.NODE);
//
//      PickInfo result = pickCanvas.pickClosest();
//
//      if(result != null){
//          if(result.getNode() instanceof Shape3D){
//
//          Color3f color;
//          Appearance app= new Appearance();
//          if(e.getButton()==1){
//            color= new Color3f(Color.red);
//          }
//          else{
//            color= new Color3f(Color.green);
//          }
//          Color3f black= new Color3f(0.0f,0.0f,0.f);
//
//          Color3f white= new Color3f(1.0f,1.0f,1.0f);
//          app.setMaterial(new Material(color,black,color,white,70f));
//          Shape3D temps = (Shape3D) result.getNode();
//          temps.setAppearance(app);
//        }
//      }
//      else{
//        System.out.println("Nothing picked");
//      }
    }


    public void mouseExited(MouseEvent e) {
//     System.out.println("mouseExiter");
//     System.out.println("mouseExiter object: " + e.getSource().getClass().getName());
    }


//
    public void mouseMoved(MouseEvent e){
//      //System.out.println("mouseMoved");
//      System.out.println(e);
//      System.out.println("mouseEntered");
//
//      System.out.println("mouseEntered object: " + e.getSource().getClass().getName());
//
//      pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
//      pickCanvas.setShapeLocation(e);
//
//      pickCanvas.setFlags(PickInfo.ALL_GEOM_INFO|PickInfo.NODE);
//
//      PickInfo result = pickCanvas.pickClosest();
//
//      if(result != null){
//          if(result.getNode() instanceof Shape3D){
//
//          Color3f color;
//          Appearance app= new Appearance();
//          if(e.getButton()==1){
//            color= new Color3f(Color.red);
//          }
//          else{
//            color= new Color3f(Color.green);
//          }
//          Color3f black= new Color3f(0.0f,0.0f,0.f);
//
//          Color3f white= new Color3f(1.0f,1.0f,1.0f);
//          app.setMaterial(new Material(color,black,color,white,70f));
//          Shape3D temps = (Shape3D) result.getNode();
//          temps.setAppearance(app);
//        }
//      }
//      else{
//        System.out.println("Nothing picked");
//      }
    }



    public String getTexturePatch() {
        return texturePatch;
    }

    public void setTexturePatch(String texturePatch) {
        this.texturePatch = texturePatch;
    }

    public String getObjectPatch() {
        return objectPatch;
    }

    public void setObjectPatch(String objectPatch) {
        this.objectPatch = objectPatch;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}