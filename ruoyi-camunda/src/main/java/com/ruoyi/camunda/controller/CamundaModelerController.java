package com.ruoyi.camunda.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDiagramDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;

/**
 * @author yaohuan
 * @version 1.0
 **/
@RestController
public class CamundaModelerController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaModelerController.class);

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 流程部署
     * @param file
     * @param processName
     * @param processId
     * @return
     */
    @PostMapping(value = "/camunda/modeler/create")
    public AjaxResult create(@RequestParam("bpmnFile") MultipartFile file, @RequestParam("processName") String processName, @RequestParam("processId") String processId) {

        try {
            String diagramName = processName + ".bpmn";
            InputStream inputStream = file.getInputStream();
            //获取Deployment对象
            Deployment deployment = repositoryService.createDeployment()
                    .name(processName)
                    .addInputStream(diagramName, inputStream)
                    .deploy();

            if (deployment != null) {
                System.out.println("部署id:" + deployment.getId());
            }
            return success("部署成功");
        } catch (IOException e) {
            LOGGER.error("根据模型部署流程失败：modelId={}", processId, e);
        }

// 用于测试有没流程定义
//        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
//                .active()
//                .processDefinitionKey(processId)
//                .latestVersion()//获取最新版本
//                .singleResult();
//
//        if (pd != null) {
//            System.out.println("流程定义id:" + pd.getId() + ",流程版本：" + pd.getVersion());
//        }
        return error("部署失败");
    }

    /**
     * 流程定义列表展示
     * @return
     */
    @PostMapping("/camunda/modeler/list")
    public TableDataInfo list() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .active()
                .latestVersion()
                .orderByProcessDefinitionKey().desc()
                .list();
        List<ProcessDefinitionDto> definitions = new ArrayList<ProcessDefinitionDto>();
        for (ProcessDefinition definition : processDefinitions) {
            ProcessDefinitionDto def = ProcessDefinitionDto.fromProcessDefinition(definition);
            definitions.add(def);
        }

        return getDataTable(definitions);
    }

    /**
     * 删除流程定义
     * @param ids
     * @return
     */
    @DeleteMapping("/camunda/modeler/remove/{ids}")
    public AjaxResult remove(@PathVariable("ids") String ids) {
        try {
            repositoryService.deleteProcessDefinition(ids);
            return AjaxResult.success();
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 根据流程定义id获取xml
     * @param processId
     * @param fileName
     * @return
     */
    @GetMapping("/camunda/modeler/xml/{ids}/{fileName}")
    public AjaxResult getProcessDefinitionBpmn20Xml(@PathVariable("ids") String processId,@PathVariable("fileName") String fileName) {
        InputStream processModelInputStream = null;
        ProcessDefinitionDiagramDto processDefinitionDiagramDto = null;
        try {
            processModelInputStream = repositoryService.getProcessModel(processId);
            byte[] processModel = IoUtil.readInputStream(processModelInputStream, fileName);
            processDefinitionDiagramDto = ProcessDefinitionDiagramDto.create(processId, new String(processModel, "UTF-8"));

            return AjaxResult.success(processDefinitionDiagramDto);
        } catch (Exception e) {
            return error(e.getMessage());
        } finally {
            IoUtil.closeSilently(processModelInputStream);
        }
    }

    /**
     * 根据key获取Xml
     * @param key
     * @return
     */
    @GetMapping("/camunda/modeler/xml/{key}")
    public AjaxResult getProcessDefinitionBpmn20XmlByKey(@PathVariable("key") String key) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .active()
                .processDefinitionKey(key)
                .latestVersion()
                .singleResult();
        if (processDefinition != null) {
            InputStream processModelInputStream = null;
            ProcessDefinitionDiagramDto processDefinitionDiagramDto = null;
            try {
                processModelInputStream = repositoryService.getProcessModel(processDefinition.getId());
                byte[] processModel = IoUtil.readInputStream(processModelInputStream, processDefinition.getName());
                processDefinitionDiagramDto = ProcessDefinitionDiagramDto.create(processDefinition.getId(), new String(processModel, "UTF-8"));

                return AjaxResult.success(processDefinitionDiagramDto);
            } catch (Exception e) {
                return error(e.getMessage());
            } finally {
                IoUtil.closeSilently(processModelInputStream);
            }
        }else {
            return error("未获取到流程定义！");
        }

    }

}
