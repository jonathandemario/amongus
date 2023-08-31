package Engine;

import org.joml.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjLoader {
    public static  Model loadModel(File f, String mtlPath) throws FileNotFoundException, IOException {
        List<Material> materials = loadMTLFile(mtlPath);
        Material currentMaterial = materials.get(0);

        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        String line;

        // \\s+ ==> Untuk catch multiple whitespace
        while((line = reader.readLine()) != null)
        {   //Parse

            // Vector Titik Biasa
            if(line.startsWith("v "))
            {
                // String Dibagi dg delimiter whitespace
                float x = Float.parseFloat(line.split("\\s+")[1]);
                float y = Float.parseFloat(line.split("\\s+")[2]);
                float z = Float.parseFloat(line.split("\\s+")[3]);
                m.vertices.add(new Vector3f(x,y,z));
            }
            // Vector Titik Normal (Shading/Lighting)
            else if(line.startsWith("vn "))
            {
                // String Dibagi dg delimiter whitespace
                float x = Float.parseFloat(line.split("\\s+")[1]);
                float y = Float.parseFloat(line.split("\\s+")[2]);
                float z = Float.parseFloat(line.split("\\s+")[3]);
                m.normals.add(new Vector3f(x,y,z));
            }
            else if(line.startsWith("f "))
            {
                Vector3f vertexIndices = new Vector3f
                        (
                                Float.parseFloat(line.split("\\s+")[1].split("/")[0]), // X
                                Float.parseFloat(line.split("\\s+")[2].split("/")[0]), // Y
                                Float.parseFloat(line.split("\\s+")[3].split("/")[0])  // Z
                        );
                Vector3f normalIndices = new Vector3f
                        (
                                Float.parseFloat(line.split("\\s+")[1].split("/")[2]), // X
                                Float.parseFloat(line.split("\\s+")[2].split("/")[2]), // Y
                                Float.parseFloat(line.split("\\s+")[3].split("/")[2])  // Z
                        );
//                System.out.println(currentMaterial.getName());
                m.faces.add(new Face(vertexIndices, normalIndices, currentMaterial.getDiffuseColor()));
            }
            else if(line.startsWith("usemtl ")) {
                String temp = line.substring(7).trim();

                for (Material mat : materials) {
                    if (mat.getName().equals(temp)) {
                        currentMaterial = mat;
                        break;
                    }
                }
            }
        }
        reader.close();
        return m;
    }

    public static List<Material> loadMTLFile(String filePath) {
        List<Material> materials = new ArrayList<>();
        Material currentMaterial = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    // Skip empty lines and comments
                    continue;
                }

                String[] tokens = line.split("\\s+");

                if (tokens[0].equals("newmtl")) {
                    // Start of a new material
                    currentMaterial = new Material();
                    currentMaterial.setName(tokens[1]);
                    currentMaterial.setAmbientColor(new Vector3f(1f, 1f, 1f));
                    materials.add(currentMaterial);
                } else if (tokens[0].equals("Kd")) {
                    // Diffuse color
                    float r = Float.parseFloat(tokens[1]);
                    float g = Float.parseFloat(tokens[2]);
                    float b = Float.parseFloat(tokens[3]);
                    currentMaterial.setDiffuseColor(new Vector3f(r, g, b));
                } else if (tokens[0].equals("Ks")) {
                    // Specular color
                    float r = Float.parseFloat(tokens[1]);
                    float g = Float.parseFloat(tokens[2]);
                    float b = Float.parseFloat(tokens[3]);
                    currentMaterial.setSpecularColor(new Vector3f(r, g, b));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for (Material material : materials) {
//            System.out.println(material.getName());;
//        }
        return materials;
    }

}