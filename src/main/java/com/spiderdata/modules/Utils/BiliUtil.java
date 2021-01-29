package com.spiderdata.modules.Utils;

import org.python.core.*;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

import java.util.zip.CRC32;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/28 11:46
 */

@Service
public class BiliUtil {
    private static String file = PathUtil.getProjectPath() + "\\src\\main\\java\\com\\spiderdata\\modules\\Utils\\AVBV.py";

    public static PyObject BvToAv(String BV) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(file);
        PyFunction pyFunction = interpreter.get("dec", PyFunction.class);
        return pyFunction.__call__(Py.newString(BV));
    }

    public static PyObject AvToBv(int AV) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(file);
        PyFunction pyFunction = interpreter.get("enc", PyFunction.class);
        return pyFunction.__call__(Py.newInteger(AV));
    }

    public static String UIDtoCrc(String UID) {
        CRC32 crc = new CRC32();
        crc.update(UID.getBytes());

        return Long.toHexString(crc.getValue());
    }
    public static int CrcToUID(String crc) {
        for(int i = 0; i < 1000000000; i++) {
            if(UIDtoCrc((String.valueOf(i))).equals(crc)) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        // 零号用户，f4dbdf21 -> 0 。2012-02-25 12:08:10 在 BV1Hx411w7XK发了很多  乾杯 - ( ゜- ゜)つロ
        // 546195 老番茄 -> 190a48b8
        // 748709 奇美拉 ->
        System.out.println(UIDtoCrc("0"));
//        System.out.println(CrcToUID("8abd66e"));
    }
}
