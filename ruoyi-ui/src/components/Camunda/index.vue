<template>
  <div class="bpmn-container">
    <div class="bpmn-canvas" ref="bpmn-canvas"></div>
    <!-- 工具栏显示的地方 -->
    <div class="bpmn-js-properties-panel" id="js-properties-panel"></div>

    <div class="bpmn-container-button">
      <el-button type="primary" @click="deployment" size="mini">保存</el-button>
      <el-button type="primary" size="mini">预览</el-button>
    </div>
  </div>

</template>

<script>

  import BpmnModeler from "bpmn-js/lib/Modeler";
  // 工具栏相关
  import propertiesProviderModule from "bpmn-js-properties-panel/lib/provider/camunda";
  import propertiesPanelModule from "bpmn-js-properties-panel";
  import camundaModdleDescriptor from "camunda-bpmn-moddle/resources/camunda";

  // 默认xml
  import DefaultEmptyXML from "@/components/Camunda/plugins/template/defaultEmpty"

  // 汉化
  import customTranslate from "@/components/Camunda/plugins/translate/customTranslate";

  import {addModeler} from "@/api/camunda/modeler";
  import {listModeler} from "@/api/activiti/modeler";
  import {format} from "@/utils/activiti/myUtil";
  import {getToken} from "@/utils/auth";

  export default {
    name: "CamundaModeler",
    computed: {},
    props: {
      xmlStr: null,
    },

    data() {
      return {
        bpmnModeler: null,
        processId: null,
        processName: null,
      }
    },
    mounted() {
      this.initBpmnModeler();
    },
    watch: {
    },
    methods: {
      async initBpmnModeler() {
        const canvas = this.$refs["bpmn-canvas"];

        // 将汉化包装成一个模块
        const customTranslateModule = {
          translate: ["value", customTranslate]
        };

        this.bpmnModeler = new BpmnModeler({
          container: canvas,

          // 加入工具栏支持
          propertiesPanel: {
            parent: "#js-properties-panel"
          },

          additionalModules: [
            // 工具栏模块
            propertiesProviderModule,
            propertiesPanelModule,
            // 汉化模块
            customTranslateModule
          ],
          moddleExtensions: {
            camunda: camundaModdleDescriptor
          }
        });
        await this.createNewDiagram(this.xmlStr);
      },
      async createNewDiagram(bpmn) {
        // 将字符串转换成图显示出来
        this.processId = `Process_${new Date().getTime()}`;
        this.processName = `业务流程_${new Date().getTime()}`;
        let xmlString = bpmn || DefaultEmptyXML(this.processId, this.processName);
        this.bpmnModeler.importXML(xmlString, err => {
          if (err) {
            this.msgError("打开模型出错,请确认该模型符合Bpmn2.0规范");
          }
        });
      },

      async deployment() {
        const {err, xml} = await this.bpmnModeler.saveXML();
        // let {href, filename} = this.setEncoded("BPMN", "leave", xml);
        // this.downloadFunc(href, filename);

        let formData = new FormData();
        formData.append("bpmnFile", new Blob([xml], {type: 'text/xml'}));
        formData.append("processName", this.processName);
        formData.append("processId", this.processId);

        addModeler(formData).then(response => {
          if (response.code === 200) {
            this.msgSuccess("新增成功");
            this.$emit("closeDialog", "yh");
          }
        });
      },

      downloadFunc(href, filename) {
        if (href && filename) {
          let a = document.createElement("a");
          a.download = filename; //指定下载的文件名
          a.href = href; //  URL对象
          a.click(); // 模拟点击
          URL.revokeObjectURL(a.href); // 释放URL 对象
        }
      },

      // 根据所需类型进行转码并返回下载地址
      setEncoded(type, filename = "diagram", data) {
        const encodedData = encodeURIComponent(data);
        return {
          filename: `${filename}.${type}`,
          href: `data:application/${type === "svg" ? "text/xml" : "bpmn20-xml"};charset=UTF-8,${encodedData}`,
          data: data
        };
      },

    }
  }
</script>

<style lang="scss" scoped>
  .bpmn-container {
    width: 100%;
    height: 100%;
    display: flex;
  }

  .bpmn-canvas {
    width: calc(100% - 300px);
    height: 100%;
    background: url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImEiIHdpZHRoPSI0MCIgaGVpZ2h0PSI0MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTTAgMTBoNDBNMTAgMHY0ME0wIDIwaDQwTTIwIDB2NDBNMCAzMGg0ME0zMCAwdjQwIiBmaWxsPSJub25lIiBzdHJva2U9IiNlMGUwZTAiIG9wYWNpdHk9Ii4yIi8+PHBhdGggZD0iTTQwIDBIMHY0MCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjZTBlMGUwIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2EpIi8+PC9zdmc+") repeat !important;

    div.toggle-mode {
      display: none;
    }
  }

  .bpmn-js-properties-panel {
    width: 320px;
    height: 100%;
    overflow-y: auto;
  }

  .bpmn-container-button {
    position: fixed;
    bottom: 20px;
    left: 20px;
  }
</style>
