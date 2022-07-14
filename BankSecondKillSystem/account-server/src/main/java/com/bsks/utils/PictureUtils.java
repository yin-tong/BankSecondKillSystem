package com.bsks.utils;


import com.bsks.api.result.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * 图片工具类（存取图像）
 */
@Component
@Slf4j
public class PictureUtils {

    public static String path;

    public void setPath(String pathYml){
        path = pathYml;
    }

    public static int size;

    public void setSize(int sizeYml){
        size = sizeYml;
    }

    public static String unit;

    public void setUnit(String unitYml){
        unit = unitYml;
    }

    /**
     * 检查图片内存大小
     * @param len
     * @param size
     * @param unit
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit)) {
            fileSize = (double) len;
        } else if ("K".equals(unit)) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit)) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit)) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }

    /**
     * 保存文件
     * @param file 文件
     * @return 文件存储路径
     */
    public static String save(MultipartFile file){
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        log.info("---PictureName:"+ fileName);
        // 获取文件名后缀
        String suffix = fileName.substring(file.getOriginalFilename().lastIndexOf("."));
        suffix = suffix.toLowerCase();
        log.info("---PictureNameSuffix:"+ suffix);
        if(!suffix.equals(".jpg") && !suffix.equals(".jpeg") && !suffix.equals(".png")){
            throw new ResultException("图片格式有误，请上传.jpg、.png、jpeg格式的文件");
        }
        //判断图片大小
        if (!checkFileSize(file.getSize(),size,unit)){
            throw new ResultException("请上传大小<"+size+unit+"的图片");
        }
        //文件重新命名
        fileName = UUID.randomUUID().toString()+suffix;
        File targetFile = new File(path, fileName);
        // 父级路径不存在就创建
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        // 保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException("图片上传失败");
        }
        return path+fileName;
    }

    /**
     * 删除图片
     * @param filePath 文件路径
     */
    public static void deleted(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
    }
}
