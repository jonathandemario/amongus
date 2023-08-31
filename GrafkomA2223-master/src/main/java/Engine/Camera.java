package Engine;

import org.joml.*;



public class Camera {

    private Vector3f direction;
    private Vector3f position;
    private Vector3f right;
    private Vector2f rotation;
    private Vector3f up;
    private Matrix4f viewMatrix;

    public Camera() {
        direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        position = new Vector3f();
        viewMatrix = new Matrix4f();
        rotation = new Vector2f();
    }

    public void updateCenterPoint(){
        Vector3f destTemp = new Vector3f();
        viewMatrix.transformPosition(0.0f,0.0f,0.0f,destTemp);
        position.x = destTemp.x;
        position.y = destTemp.y;
        position.z = destTemp.z;
//        System.out.println(centerPoint.get(0));
    }

    public void addRotation(float x, float y) {
        rotation.add(x, y);

        recalculate();
    }

    public Vector2f getRotation() {
        return rotation;
    }

    public Vector3f getRight() {
        return right;
    }

    public Vector3f getUp() {
        return up;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void moveBackwards(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.sub(direction);
        recalculate();
    }
    public void moveForward(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.add(direction);
        recalculate();
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void moveLeft(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.sub(right);
        recalculate();
    }
    public void moveRight(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.add(right);
        recalculate();
    }

    public void revolve(float inc){
        if (inc < 0){
            moveLeft(inc);
        }
        if (inc > 0){
            moveRight(inc);
        }
    }

    public void moveUp(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.add(up);
        recalculate();
    }
    public void moveDown(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.sub(up);
        recalculate();
    }

    private void recalculate() {
        viewMatrix.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
    }

    public void translateCamera(Float offsetX,Float offsetY,Float offsetZ){
        viewMatrix = new Matrix4f().translate(-offsetX,-offsetY,-offsetZ).mul(new Matrix4f(viewMatrix));
        updateCenterPoint();
    }

    public void move(float x, float y, float z){
        translateCamera(x,y,z);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    }

    public void setRotation(float x, float y) {
        rotation.set(x, y);
        recalculate();
    }

    public void rotateCamera(Float degree, Float x,Float y,Float z){
        viewMatrix = new Matrix4f().rotate(degree,-x,-y,-z).mul(new Matrix4f(viewMatrix));
        updateCenterPoint();
    }
}