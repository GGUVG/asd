package com.demo.asd.api.controller.house.forSale;

import com.demo.asd.dataResult.DataResult;
import com.demo.asd.dataResult.DataResults;
import com.demo.asd.model.house.forSale.HouseSaleWithOwnerClientRequest;
import com.demo.asd.model.house.forSale.HouseSaleWithOwnerClientResponse;
import com.demo.asd.model.house.report.HouseSaleSourceApproveRequest;
import com.demo.asd.pagination.PagingRequest;
import com.demo.asd.pagination.PagingResponse;
import com.demo.asd.service.bizs.house.forSale.HouseSaleBiz;
import com.demo.asd.support.model.po.sys.UploadFile;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
/**
                       .::::.
                     .::::::::.
                   :::::::::::
                 ..:::::::::::'
              '::::::::::::'
                .::::::::::
           '::::::::::::::..
                ..::::::::::::.
              ``::::::::::::::::
               ::::``:::::::::'        .:::.
              ::::'   ':::::'       .::::::::.
            .::::'      ::::     .:::::::'::::.
           .:::'       :::::  .:::::::::' ':::::.
          .::'        :::::.:::::::::'      ':::::.
         .::'         ::::::::::::::'         ``::::.
     ...:::           ::::::::::::'              ``::.
    ```` ':.          ':::::::::'                  ::::..
                       '.:::::'                    ':'````..
**/
@Validated
@RestController
@RequestMapping(value="/v1/house/forSale")
public class HouseSaleController {
    @Autowired
    public HouseSaleBiz houseSaleBiz;

    @ApiOperation(tags = "HouseSaleWithOwnerClient", value = "findPageHouseForSale", httpMethod = "POST",
            notes = "分页查询买卖房源信息")
    @PostMapping("/findPageHouseForSale")
    public DataResult<PagingResponse<HouseSaleWithOwnerClientResponse>> findPageHouseForSale(
            @RequestBody PagingRequest<HouseSaleWithOwnerClientRequest> req, HttpServletRequest hReq) throws IllegalAccessException, InstantiationException, UnsupportedEncodingException {
        return DataResults.ok(houseSaleBiz.findPageHouseForSale(req,hReq));
    }

    /**
     * 买卖房源信息导出excel
     */
    @ApiOperation(tags = "HouseSaleClientResponse", value = "exportFindHouseForSale", httpMethod = "POST",
            notes = "买卖房源信息导出excel")
    @PostMapping("/exportFindHouseForSale")
    public void exportFindHouseForSale(HttpServletResponse hRep, HttpServletRequest hReq,@RequestBody HouseSaleWithOwnerClientRequest condition) throws IllegalAccessException, InstantiationException, UnsupportedEncodingException
    {
        houseSaleBiz.exportFindPageHouseForSale(hRep,hReq,condition);
    }

    @ApiOperation(tags = "HouseRentSourceApproveBean", value = "reportNewSource", httpMethod = "POST",
            notes = "报备新房源信息")
    @PostMapping("/reportNewSource")
    public DataResult<Integer> reportNewSource(
            @RequestBody HouseSaleSourceApproveRequest request, HttpServletRequest hReq) throws IOException
    {
        return DataResults.ok(houseSaleBiz.reportNewSource(request,hReq));
    }

    @ApiOperation(tags = "HouseRentSourceApproveBean", value = "reportNewSource", httpMethod = "POST",
            notes = "上传新房源文件")
    @PostMapping("/uploadHouseSaleFile")
    public String uploadHouseSaleFile(@RequestParam("newFile1") MultipartFile newFile,
                                      @RequestParam("houseEstateId")Long houseEstateId,
                                      @RequestParam("houseName")String houseName,
                                      @RequestParam("materialTypeTxt")String materialTypeTxt,
                                      @RequestParam("houseLocationProvince")String houseLocationProvince,
                                      @RequestParam("houseLocationCity")String houseLocationCity,
                                      @RequestParam("houseLocationDistrict")String houseLocationDistrict,
                                      @RequestParam("houseLocationStreet")String houseLocationStreet,
                                      @RequestParam("staffId")Long staffId,
                                      @RequestParam("staffUserName")String staffUserName) throws IOException
    {
        return houseSaleBiz.uploadHouseSaleFile(newFile,houseEstateId,houseName,materialTypeTxt,houseLocationProvince,houseLocationCity,houseLocationDistrict,houseLocationStreet,staffId,staffUserName);
    }

    @ApiOperation(tags = "delHouseSaleFile", value = "reportNewSource", httpMethod = "POST",
            notes = "删除上传的新房源文件")
    @PostMapping("/delHouseSaleFile")
    public void delHouseSaleFile(@RequestBody UploadFile uploadFile) throws IOException
    {
        houseSaleBiz.delHouseSaleFile(uploadFile.getFileName());
    }
}
