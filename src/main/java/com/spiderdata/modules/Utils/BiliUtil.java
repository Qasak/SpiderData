package com.spiderdata.modules.Utils;

import org.python.core.*;
import org.python.util.PythonInterpreter;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/28 11:46
 */
public class BiliUtil {
    private static String file = PathUtil.getClassPath() + "AVBV.py";

    public static PyObject BvToAv(String BV) {
        PythonInterpreter interpreter = new PythonInterpreter();
        System.out.println(file);
        interpreter.execfile(file);
        PyFunction pyFunction = interpreter.get("dec", PyFunction.class);
        PyObject pyobj = pyFunction.__call__(Py.newString(BV));
        return pyobj;
    }

    public static PyObject AvToBv(int AV) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(file);
        PyFunction pyFunction = interpreter.get("enc", PyFunction.class);
        PyObject pyobj = pyFunction.__call__(Py.newInteger(AV));
        return pyobj;
    }
    public static void main(String[] args) {
        System.out.println(BvToAv("BV1qs41117pt"));
    }
}
