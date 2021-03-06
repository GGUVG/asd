package com.demo.asd.service.bizs.client;

import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.demo.asd.base.hierarchies.BaseBiz;
import com.demo.asd.base.hierarchies.BaseService;
import com.demo.asd.beanUtils.BeanUtils;
import com.demo.asd.excel.EasyExcelUtils;
import com.demo.asd.model.client.forRent.ClientRentRequest;
import com.demo.asd.model.client.forRent.ClientRentResponse;
import com.demo.asd.pagination.PageConverter;
import com.demo.asd.pagination.Pagination;
import com.demo.asd.pagination.PagingRequest;
import com.demo.asd.pagination.PagingResponse;
import com.demo.asd.service.services.client.ClientForRentService;
import com.demo.asd.support.model.po.client.forRent.ClientRentBean;
import com.demo.asd.support.model.po.client.forRent.ClientRentCriteria;
import com.demo.asd.support.model.po.client.forRent.ClientRentExcelBean;
import com.demo.asd.support.model.po.staff.StaffCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CLientForRentBiz extends BaseBiz<Long, ClientRentBean, ClientRentCriteria, ClientRentRequest, ClientRentResponse>
{
    @Autowired
    public ClientForRentService clientForRentService;

    public PagingResponse<ClientRentResponse> findPageClientRent(PagingRequest<ClientRentRequest> pag, HttpServletRequest hReq) throws UnsupportedEncodingException, IllegalAccessException, InstantiationException {
        ClientRentCriteria criteria= BeanUtils.copy(pag.getCriteria(), this.criteriaClass);
        Cookie[] cookies=hReq.getCookies();
        for(Cookie cookie:cookies)
        {
            String checkStr="backStaffCookie";
            if(checkStr.equals(cookie.getName()))
            {
                String str1= URLDecoder.decode(cookie.getValue(), "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(str1);
                StaffCriteria staffCriteria=JSONObject.parseObject(jsonObject.toJSONString(),new TypeReference<StaffCriteria>() {});
                criteria.setClientStaffId(staffCriteria.getStaffId());
            }
        }
        Pagination pagination =BeanUtils.copy(pag.getPagination(), Pagination.class);
        List<ClientRentBean> beans=clientForRentService.findClientRentByPage(criteria,pagination);
        long count=clientForRentService.countFindClientRent(criteria);
        PagingResponse<ClientRentResponse> response = PageConverter.convert(pagination, ClientRentResponse.class, count, beans);
        return response;
    }

    public String exportFindCLientRent(HttpServletResponse hRep, HttpServletRequest hReq, ClientRentRequest condition) throws UnsupportedEncodingException
    {
        Cookie[] cookies=hReq.getCookies();
        for(Cookie cookie:cookies)
        {
            String checkStr="backStaffCookie";
            if(checkStr.equals(cookie.getName()))
            {
                String str1=URLDecoder.decode(cookie.getValue(), "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(str1);
                StaffCriteria staffCriteria=JSONObject.parseObject(jsonObject.toJSONString(),new TypeReference<StaffCriteria>() {});
                condition.setClientStaffId(staffCriteria.getStaffId());
            }
        }
        List<ClientRentBean> beans=clientForRentService.findClientRentForRequest(condition);
        List<ClientRentExcelBean> reportBeans=BeanUtils.copyList(beans, ClientRentExcelBean.class);
        Map<String, List<? extends BaseRowModel>> map = new HashMap<>();
        map.put("ClientForRent", reportBeans);
        String fileName = new String(("放租房源客户信息" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())).getBytes(), "UTF-8");
        EasyExcelUtils.createExcelStreamMutilByEaysExcel(hRep,fileName, map, ExcelTypeEnum.XLSX);
        return fileName;
    }

    @Override
    public BaseService<Long, ClientRentBean, ClientRentCriteria> getService()
    {
        return clientForRentService;
    }
}
