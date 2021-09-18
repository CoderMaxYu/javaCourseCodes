import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloXlassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            Class hello = new HelloXlassLoader().findClass("Hello");
            Method helloMethod = hello.getDeclaredMethod("hello");
            helloMethod.invoke(hello.newInstance(), null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = toByteArray(name, "xlass");
        //转码 n= 255-n
        for (int i = 0; i < data.length; i++) {
//            data[i] = (byte) (((byte) 255) - data[i]);
            data[i] = (byte) (255 - data[i]);
        }
        return defineClass(name, data, 0, data.length);
    }


    /**
     * 文件转byte[]
     * @param fileName 文件名
     * @param fileSuffix 后缀
     * @return
     */
    private byte[] toByteArray(String fileName, String fileSuffix) {
        try {
            InputStream in = new FileInputStream(getClass().getResource("/").getPath() + fileName + "." + fileSuffix);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


}