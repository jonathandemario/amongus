package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Sphere extends Circle{
    float radiusZ;
    int stackCount;
    int sectorCount;
    List<Vector3f> normal;
    float angle;
    int nbo;
    int nboColor;
    Model m;
//    public Sphere(String type, List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, List<Float> centerPoint, Float radiusX, Float radiusY, Float radiusZ,
//                  int sectorCount,int stackCount, float angle) {
//        super(shaderModuleDataList, vertices, color, centerPoint, radiusX, radiusY);
//        this.radiusZ = radiusZ;
//        this.stackCount = stackCount;
//        this.sectorCount = sectorCount;
//        this.angle = angle;
//        switch (type){
//            case "map" : loadMap();break;
//        }
//        setupVAOVBO();
//    }

    public Sphere(String type, List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, List<Vector3f> color, List<Float> centerPoint, Float radiusX, Float radiusY, Float radiusZ,
                  int sectorCount,int stackCount, float angle) {
        super(shaderModuleDataList, vertices, color, centerPoint, radiusX, radiusY);

        this.radiusZ = radiusZ;
        this.stackCount = stackCount;
        this.sectorCount = sectorCount;
        this.angle = angle;
        switch (type){
            case "map" : loadObject("resources/models/map.obj", "resources/models/map.mtl");break;
            case "player" : loadObject("resources/models/amongus.obj", "resources/models/amongus.mtl");break;
            case "knife" : loadObject("resources/models/knife.obj", "resources/models/knife.mtl");break;
            case "deadbody" : loadObject("resources/models/dead_body.obj", "resources/models/dead_body.mtl");break;
        }
        setupVAOVBOWithVerticesColor();
    }

    public void loadObject(String obj, String mtl) {
        vertices.clear();

        try {
            m = ObjLoader.loadModel(new File(obj), mtl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        normal = new ArrayList<>();
        for(Face face : m.faces){
            Vector3f n1 = m.normals.get((int) face.normal.x-1);
            normal.add(n1);
            Vector3f v1 = m.vertices.get((int) face.vertex.x-1);
            vertices.add(v1);
            verticesColor.add(face.color);

            Vector3f n2 = m.normals.get((int) face.normal.y-1);
            normal.add(n2);
            Vector3f v2 = m.vertices.get((int) face.vertex.y-1);
            vertices.add(v2);
            verticesColor.add(face.color);

            Vector3f n3 = m.normals.get((int) face.normal.z-1);
            normal.add(n3);
            Vector3f v3 = m.vertices.get((int) face.vertex.z-1);
            vertices.add(v3);
            verticesColor.add(face.color);
        }
    }

    public void setupVAOVBO() {
        super.setupVAOVBO();
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normal),
                GL_STATIC_DRAW);
    }

    public void setupVAOVBOWithVerticesColor(){
        super.setupVAOVBOWithVerticesColor();
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normal),
                GL_STATIC_DRAW);

        //set nboColor
        nboColor = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nboColor);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(verticesColor),
                GL_STATIC_DRAW);
    }

    public void drawSetup(Camera camera, Projection projection, Vector3f ambientStrength){
        super.drawSetup(camera,projection, ambientStrength);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1, 3,
                GL_FLOAT,
                false,
                0, 0);
        //directional Light
        uniformsMap.setUniform("lightColor",new Vector3f(1.0f,1.0f,0.0f));
        uniformsMap.setUniform("lightPos",new Vector3f(1.0f,1.0f,0.0f));
        uniformsMap.setUniform("viewPos",camera.getPosition());
    }

    public void drawSetupWithVerticesColor(Camera camera, Projection projection, boolean blackout){
        super.drawSetupWithVerticesColor(camera, projection, blackout);

        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nboColor);
        glVertexAttribPointer(1, 3,
                GL_FLOAT,
                false,
                0, 0);

        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(2, 3,
                GL_FLOAT,
                false,
                0, 0);

        Vector3f[] hallLights = {
                // hall medbay
                new Vector3f(-3.55f, 0.25f, -1.5f),
                new Vector3f(-4.8f, 0.25f, -1.5f),
                new Vector3f(-3.55f, 0.25f, -0.5f),
                new Vector3f(-4.45f, 0.25f, -0.5f),

                // medbay
                new Vector3f(-5.9f, 0.25f, 2.4f),
                new Vector3f(-5.6f, 0.25f, 2.8f),
                new Vector3f(-5.4f, 0.25f, 3.2f),
                new Vector3f(-4.35f, 0.25f, 3.2f),
                new Vector3f(-3.3f, 0.25f, 3.2f),
                new Vector3f(-2.6f, 0.25f, 3f),

                // main hall
                new Vector3f(-3f, 0.25f, -2f),
                new Vector3f(-3f, 0.25f, -0.1f),
                new Vector3f(-2.95f, 0.25f, 1.15f),

                new Vector3f(-1.9f, 0.25f, 2.15f),
                new Vector3f(-1.25f, 0.25f, 2.35f),
                new Vector3f(-0.45f, 0.25f, 2.35f),
                new Vector3f(0.43f, 0.25f, 2.35f),
                new Vector3f(1.4f, 0.25f, 2.35f),

                new Vector3f(-3f, 0.25f, -3.1f),
                new Vector3f(-2f, 0.25f, -4f),
                new Vector3f(2f, 0.25f, -4.05f),
                new Vector3f(2.25f, 0.25f, 2.15f),
                new Vector3f(3f, 0.25f, 1.3f),
                new Vector3f(3.5f, 0.25f, 0.7f),
                new Vector3f(3.5f, 0.25f, -0.3f),
                new Vector3f(3.5f, 0.25f, -1.3f),
                new Vector3f(3.5f, 0.25f, -2.15f),
            };

            Vector3f[] monitorLights = {
                    new Vector3f(-3.15f, 0.25f, 2.15f),
                    new Vector3f(0.355600f, 0.800000f, 0.424038f),
            };

        if (!blackout) {
            //directional Light
            uniformsMap.setUniform("dirLight.direction", new Vector3f(0f, 0f, 0f));
            uniformsMap.setUniform("dirLight.ambient", new Vector3f(.25f, .25f, .25f));
            uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
            uniformsMap.setUniform("dirLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));

            for (int i = 0; i < hallLights.length; i++) {
                uniformsMap.setUniform("pointLights[" + i + "].position", hallLights[i]);
                uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0f, 0f, 0f));
                uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(1f, 1f, 1f));
                uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(0.75f, 0.75f, 0.75f));
                uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
                uniformsMap.setUniform("pointLights[" + i + "].linear", 0.7f);
                uniformsMap.setUniform("pointLights[" + i + "].quadratic", 1.8f);
            }

            for (int i = 0; i < monitorLights.length; i+=2) {
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].position", monitorLights[i]);
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].ambient", new Vector3f(-0.5f,-0.5f,-0.5f));
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].diffuse", monitorLights[i+1]);
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].specular", new Vector3f(1f, 1f, 1f));
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].constant", 1.0f);
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].linear", 0.7f);
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].quadratic", 1.8f);
            }
            // no spotlight
            uniformsMap.setUniform("spotLight.position", camera.getPosition());
            uniformsMap.setUniform("spotLight.direction", new Vector3f(
                    camera.getViewMatrix().get(2,0),
                    camera.getViewMatrix().get(2,1),
                    -camera.getViewMatrix().get(2,2)
            ));
            uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
            uniformsMap.setUniform("spotLight.diffuse", new Vector3f(0.0f, 0.0f, 0.0f));
            uniformsMap.setUniform("spotLight.specular", new Vector3f(0.0f, 0.0f, 0.0f));
            uniformsMap.setUniform("spotLight.constant", 1.0f);
            uniformsMap.setUniform("spotLight.linear", 0.00f);
            uniformsMap.setUniform("spotLight.quadratic", 0.00f);
            uniformsMap.setUniform("spotLight.cutOff", (float) Math.cos(Math.toRadians(20f)));
            uniformsMap.setUniform("spotLight.outerCutOff", (float) Math.cos(Math.toRadians(21f)));
        }

        else {
            uniformsMap.setUniform("dirLight.direction", new Vector3f(0f, 0f, 0f));
            uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
            uniformsMap.setUniform("dirLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));

            for (int i = 0; i < hallLights.length; i++) {
                uniformsMap.setUniform("pointLights[" + i + "].position", hallLights[i]);
                uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0f, 0f, 0f));
                uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(0f, 0f, 0f));
                uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(0f, 0f, 0f));
                uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
                uniformsMap.setUniform("pointLights[" + i + "].linear", 0.7f);
                uniformsMap.setUniform("pointLights[" + i + "].quadratic", 1.8f);
            }

            for (int i = 0; i < monitorLights.length; i+=2) {
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].position", monitorLights[i]);
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].ambient", new Vector3f(0f, 0f, 0f));
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].diffuse", new Vector3f(0f, 0f, 0f));
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].specular", new Vector3f(0f, 0f, 0f));
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].constant", 1.0f);
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].linear", 0.7f);
                uniformsMap.setUniform("pointLights[" + (i/2+27) + "].quadratic", 1.8f);
            }

            // spotlight on
            uniformsMap.setUniform("spotLight.position", camera.getPosition());
            uniformsMap.setUniform("spotLight.direction", new Vector3f(
                    camera.getViewMatrix().get(2,0),
                    camera.getViewMatrix().get(2,1),
                    -camera.getViewMatrix().get(2,2)
            ));
            uniformsMap.setUniform("spotLight.ambient",new Vector3f(.05f, .05f, .05f));
            uniformsMap.setUniform("spotLight.diffuse",new Vector3f(1.0f,1.0f,1.0f));
            uniformsMap.setUniform("spotLight.specular",new Vector3f(.75f, .75f, .75f));
            uniformsMap.setUniform("spotLight.constant",1.0f);
            uniformsMap.setUniform("spotLight.linear",0.09f);
            uniformsMap.setUniform("spotLight.quadratic",0.032f);
            uniformsMap.setUniform("spotLight.cutOff",(float)Math.cos(Math.toRadians(12f)));
            uniformsMap.setUniform("spotLight.outerCutOff",(float)Math.cos(Math.toRadians(12.5f)));
        }
        uniformsMap.setUniform("viewPos",camera.getPosition());
    }
}