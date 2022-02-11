package com.gwhn.elasticsearch.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gwhn.elasticsearch.entity.SatellitePathWay;
import com.gwhn.elasticsearch.service.SatellitePathWay.SatellitePathWayService;
import com.gwhn.elasticsearch.util.DateUtil;
import com.gwhn.elasticsearch.util.IdUtil;
import com.gwhn.elasticsearch.util.YereeXmlContext;
import net.sf.json.JSONArray;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author banxian
 * @date 2021/1/14 9:38
 */
@RestController
@RequestMapping("/api/satellitepathway")
public class SatellitePathWayController {

    @Resource(name = "satellitePathWayServiceImpl")
    private SatellitePathWayService satellitePathWayService;


    /**
     * 条件查询排序分页
     *
     * @param satellitePathWay
     * @param page
     * @param order
     * @param rows
     * @param sort
     * @return
     * @author banxian
     * @date: 2021/1/21 14:30
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public IPage<SatellitePathWay> getSatellitePathWayListByPage(SatellitePathWay satellitePathWay, Integer page, Integer rows, String sort, String order) {
        System.out.println("第" + page + "页");
        System.out.println("每页" + rows + "个");
        System.out.println("sort:" + sort);
        System.out.println("order:" + order);
        return satellitePathWayService.getSatellitePathWayListByPage(satellitePathWay, page, rows, sort, order);
    }

    /**
     * 新增单个轨道报
     *
     * @param satellitePathWay
     * @return
     * @author banxian
     * @date: 2021/1/21 14:31
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public int addSingle(SatellitePathWay satellitePathWay) {
        if (satellitePathWayService.addOrUpdateSatellitePathWay(satellitePathWay)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 查询卫星名字返回给下拉列表
     *
     * @return
     * @author banxian
     * @date: 2021/1/21 14:32
     */
    @ResponseBody
    @RequestMapping(value = "/namelist", method = RequestMethod.POST)
    public String getSatellitaPathWayNameList() {
        List<String> stringList = satellitePathWayService.getSatellitePathWayNameList();
        List<SatellitePathWay> satellitePathWayList = new ArrayList<>();
        for (String satelliteName : stringList) {
            SatellitePathWay satellitePathWay = new SatellitePathWay();
            satellitePathWay.setSatelliteName(satelliteName);
            satellitePathWayList.add(satellitePathWay);
        }
        JSONArray jsonArray = JSONArray.fromObject(satellitePathWayList);
        return jsonArray.toString();
    }

    /**
     * 删除轨道报
     *
     * @return Y/N
     * @author banxian
     * @date: 2021/1/21 14:32
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteSatellitaPathWayByIds(@RequestParam(value = "ids") List<String> ids) {
        try {
            satellitePathWayService.deleteSatellitePathWayList(ids);
            return "Y";
        } catch (Exception e) {
            return "N";
        }
    }

    /**
     * 批量新增轨道报
     *
     * @param data
     */
    @RequestMapping("/add/list")
    public String addFileList(@RequestParam("fileShhd[]") MultipartFile[] data) {
        int resutlCount = 0;
        List<SatellitePathWay> satellitePathWayList = new ArrayList<SatellitePathWay>();
        List<SatellitePathWay> list = new ArrayList<>();
        List pathList = new ArrayList();

        String YYYY;
        String MM;
        String DD;
        String HH;

        InputStream pathList1 = null;
        for (int i = 0; i < data.length; i++) {
            String fileName = data[i].getOriginalFilename();//文件名
            String fileType = data[i].getContentType();//文件类型
            try {
                pathList1 = data[i].getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //保存路径
            //springboot 默认情况下只能加载 resource文件夹下静态资源文件
            YereeXmlContext yereeXmlContext = new YereeXmlContext();
            String path = yereeXmlContext.getXmlValue("Http","FileUpload","uploadSatellitepathwayWordPath");
            File parentFile = new File(path);
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            //生成保存文件
            IdUtil idUtil = new IdUtil();
            File uploadFile = new File(path + idUtil.nextId()+ DateUtil.getCurrentDateTime("yyyyMMddHHmm")+fileName);
            String paths = String.valueOf(uploadFile);
            System.out.println(uploadFile);
            //将上传文件保存到路径
            try {
                data[i].transferTo(uploadFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List infoDoc = Collections.singletonList(uploadFile);
            pathList.add(paths);

            try {
                /**
                 * path:"D:\\ideaworkspace\\2019年07月08日卫星过境预报表.doc"
                 */

                FileInputStream fileInputStream = new FileInputStream(paths);
                String year = paths.substring(paths.indexOf("年") - 4, paths.indexOf("年"));//从text中截取年
                String month = paths.substring(paths.indexOf("月") - 2, paths.indexOf("月"));//从text中截取月
                String day = paths.substring(paths.indexOf("日") - 2, paths.indexOf("日"));//从text中截取日
                if (month.contains("年")) {
                    month = "0" + month.substring(1, 2);
                }
                if (day.contains("月")) {
                    day = day.substring(1, 2);
                }
                String checkTime = year + "-" + month + "-" + day + " 08:00" + ":00";//时间字符串2019/07/09  08:00
                POIFSFileSystem pfs = new POIFSFileSystem(fileInputStream);
                HWPFDocument hwpfDocument = new HWPFDocument(pfs);
                Range range = hwpfDocument.getRange();//get读取范围
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String hwpfDocumentText = hwpfDocument.getText().toString();
                String tabulattonperson = hwpfDocumentText.substring(hwpfDocumentText.indexOf("制表：") + 3, hwpfDocumentText.indexOf("审批：") - 1);
                String approveperson = hwpfDocumentText.substring(hwpfDocumentText.indexOf("审批：") + 3, hwpfDocumentText.indexOf("序号") - 2);
//                String addtime = dateFormat.parse(now);//添加时间
                TableIterator tableIterator = new TableIterator(range);
                int m = 0;

                while (tableIterator.hasNext()) {
                    Table table = tableIterator.next();//迭代
                    for (int l = 0; l < table.numRows(); l++) {
                        if (l > 0) {
                            System.out.println();
                            TableRow tr = table.getRow(l);//提取第i行
                            SatellitePathWay satellitePathWay = new SatellitePathWay();//每行都是一个对象
                            String wordTime = null;
                            String outcheckTime = null;
                            for (int j = 1; j < tr.numCells(); j++) {
                                TableCell td = tr.getCell(j);//提取第i行第j格
                                for (int k = 0; k < td.numParagraphs(); k++) {//td.numParagraphs()单元格宽度
                                    Paragraph paragraph = td.getParagraph(k);
                                    String word = paragraph.text().trim();//转换完去空格

                                    if (j == 1) {//站点
                                        satellitePathWay.setSatelliteAcceptsite(word);
                                    }
                                    if (j == 2) {//卫星名称
                                        satellitePathWay.setSatelliteName(word);
                                    }
                                    if (j == 3) {//入境时间
                                        String years = word.split(" ")[0];//从text中截取年月日
                                        String days = word.split(" ")[1];//从text中获取时分秒

                                        YYYY = years.split("/")[0];

                                        if (years.split("/")[1].length() == 1) {
                                            MM = "0" + years.split("/")[1];
                                        } else {
                                            MM = years.split("/")[1];
                                        }
                                        if (years.split("/")[2].length() == 1) {
                                            DD = "0" + years.split("/")[2];
                                        } else {
                                            DD = years.split("/")[2];
                                        }

                                        if (days.split(":")[0].length() == 1) {
                                            HH = "0" + days.split(":")[0];
                                        } else {
                                            HH = days.split(":")[0];
                                        }
                                        String mm = days.split(":")[1];
                                        String checkTimes = YYYY + "-" + MM + "-" + DD + " " + HH + ":" + mm + ":00";
                                        satellitePathWay.setEnterTime(dateFormat.parse(checkTimes));
                                        wordTime = checkTimes;
                                    }
                                    if (j == 4) {//出境时间
                                        String YYYYMMDD = tr.getCell(3).getParagraph(0).text().trim().split(" ")[0];
                                        YYYY = YYYYMMDD.split("/")[0];

                                        if (YYYYMMDD.split("/")[1].length() == 1) {
                                            MM = "0" + YYYYMMDD.split("/")[1];
                                        } else {
                                            MM = YYYYMMDD.split("/")[1];
                                        }
                                        if (YYYYMMDD.split("/")[2].length() == 1) {
                                            DD = "0" + YYYYMMDD.split("/")[2];
                                        } else {
                                            DD = YYYYMMDD.split("/")[2];
                                        }

                                        if (word.split(":")[0].length() == 1) {
                                            HH = "0" + word.split(":")[0];
                                        } else {
                                            HH = word.split(":")[0];
                                        }
                                        String mm = word.split(":")[1];
                                        String ss = word.split(":")[2];
                                        outcheckTime = YYYY + "-" + MM + "-" + DD + " " + HH + ":" + mm + ":" + ss;
                                        satellitePathWay.setOutTime(dateFormat.parse(outcheckTime));
                                    }
                                    if (j == 5) {//扫描范围：中国西部（不含湖南）
                                        String scanArea = word.substring(0, word.indexOf("（"));
                                        satellitePathWay.setScanArea(scanArea);//中国西部
                                        String wehtherIncludeHunan = word.substring(scanArea.length() + 1, word.length() - 1);
                                        String HANHUNAN = "含湖南";
                                        if (wehtherIncludeHunan == HANHUNAN) {
                                            satellitePathWay.setWhetherIncludeHunan("1");//是否包含湖南
                                        } else {
                                            satellitePathWay.setWhetherIncludeHunan("0");//是否包含湖南
                                        }
                                    }
                                    satellitePathWay.setTabulationPerson(tabulattonperson.trim());//制表人
                                    satellitePathWay.setApprovePerson(approveperson.trim());//审批人

                                    satellitePathWay.setAddTime(now);//制表时间 addtime
                                }
//                                satellitePathWayService.deletequery(dateFormat.parse(wordTime),dateFormat.parse(outcheckTime),tr.getCell(2).getParagraph(1).text().trim());
                            }
                            if (compareTime(checkTime, wordTime)) {
//                                satellitePathWayService.deletequery(satellitePathWay.getEntertime(),satellitePathWay.getOuttime(),satellitePathWay.getSatellitename());
                                satellitePathWayList.add(satellitePathWay);
                                resutlCount ++;
                            } else {
                                m++;//当轨道报上存在卫星过境时间早于上午8点时，这部分卫星不存入数据库,年月日+(序号-m)就是id.....谁瞎写的轨道报找谁去，@制表人
                            }
                        }
                    }
                }
                satellitePathWayService.addOrUpdateSatellitePathWayList(satellitePathWayList);
            } catch (FileNotFoundException e) {
                e.printStackTrace();//文件未找到
            } catch (IOException e) {
                e.printStackTrace();//文件格式不对
            } catch (Exception e) {
                e.printStackTrace();//其他异常
            }
        }
        return resutlCount+"";
    }

    /**
     * 比较两个字符串形式的时间，time1<time2返回true
     *
     * @param time1,time2
     * @return true/false
     */
    public boolean compareTime(String time1, String time2) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化
        Date date1 = simpleDateFormat.parse(time1);//时间转日期
        Date date2 = simpleDateFormat.parse(time2);
        if (date1.compareTo(date2) < 0) {
            return true;
        } else {
            return false;
        }
    }
}
