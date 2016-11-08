package com.wangpeng.beautifullife.utils.base;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by d1yf.132 on 2016/8/15.
 */
public class BaseUtils {
    private Context _context;

    public BaseUtils(Context context){
        this._context = context;
    }

    /**
     * 文件复制
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    public int CopyFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public void saveFile(String fileName){
        File file = _context.getFilesDir();
        file = new File(file.getAbsolutePath()+fileName);
        System.out.println(file.getAbsolutePath());
    }

    public int[] getScreen(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels,dm.heightPixels};
    }
}
