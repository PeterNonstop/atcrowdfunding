package com.atguigu.crowd.handler;

import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.service.api.ProjectService;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author Peter
 * Date: 2021/9/11 22:49
 * To change this template use File | Settings | Editor | File Templates.
 * Description:
 *
 */
@RestController
public class ProjectProviderHandler {

    @Autowired
    private ProjectService projectService;

    private final Logger logger = LoggerFactory.getLogger(ProjectProviderHandler.class);


    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(
            @PathVariable("projectId") Integer projectId) {
        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }



    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote() {

        try {
            List<PortalTypeVO> portalTypeVOList = projectService.getPortalTypeVO();

            return ResultEntity.successWithData(portalTypeVOList);

        } catch (Exception exception) {
            exception.printStackTrace();

            return ResultEntity.failed(exception.getMessage());
        }

    }


    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
                                             @RequestParam("memberId") Integer memberId){

        try {
            // 调用“本地” Service 执行保存
            projectService.saveProject(projectVO, memberId);

            return ResultEntity.successWithoutData();

        } catch (Exception e) {

            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }
    }

}
