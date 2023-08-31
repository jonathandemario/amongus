#version 330

out vec4 fragColor;

uniform vec3 lightColor;
uniform vec3 lightPos;
uniform vec3 viewPos;

in vec3 Normal;
in vec3 FragPos;
in vec4 out_color;
void main()
{
    // ambient
    float ambientStrength = 0.5;
    vec3 ambient = ambientStrength * lightColor;

    // diffuse
    vec3 lightDir = normalize(lightPos - FragPos);
    float diff = max(dot(Normal, lightDir), 0.0f);
    vec3 diffuse = diff * lightColor;

    // specular
    float specularStrength = 0.8;
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(0.0, dot(Normal, halfwayDir)), 64*3);

    vec3 result = (ambient + diffuse + spec) * vec3(out_color);
    fragColor = vec4(result, 1.0);
}

