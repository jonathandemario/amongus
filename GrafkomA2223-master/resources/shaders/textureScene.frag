#version 330 core

in vec2 fragTexCoord;
in vec3 Normal;
in vec3 FragPos;
in vec4 out_color;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform vec3 lightPos;
uniform vec3 viewPos;
//uniform vec3 objectColor;

void main()
{
    // Retrieve the texture color
    vec4 textureColor = texture(textureSampler, fragTexCoord);

    // Calculate the ambient lighting component
    float ambientStrength = 1;
    vec3 ambient = ambientStrength * lightColor;

    // Calculate the diffuse lighting component
    vec3 lightDirection = normalize(lightPos - FragPos);
    float diffuseFactor = max(dot(Normal, lightDirection), 0.0);
    vec3 diffuse = diffuseFactor * lightColor;

    // specular
    float specularStrength = 0.5;
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 halfwayDir = normalize(lightDirection + viewDir);
    float spec = pow(max(0.0, dot(Normal, halfwayDir)), 64*3);

    // Combine the ambient, diffuse, and specular components
    vec3 finalColor = (1 + 0.8 + 0.5) * vec3(out_color);
    fragColor = vec4(finalColor, 1.0);
}
