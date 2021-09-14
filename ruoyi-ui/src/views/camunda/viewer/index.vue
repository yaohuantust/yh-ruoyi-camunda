<template>
  <div class="bpmn-container">
    <!--    <el-button type="primary" @click="previewTemp">预览</el-button>-->
    <!--    <el-button type="success" @click="handleZoom(1)">放大</el-button>
        <el-button type="warning" @click="handleZoom(-1)">缩小</el-button>-->
        <div id="bpmn-canvas"
             v-bind:style="{width: 100 * scale + '%',height: 100 * scale + '%'}"
        ></div>
  </div>
</template>

<script>
  import BpmnViewer from 'bpmn-js/lib/Viewer';


  export default {
    name: "CamundaViewer",
    props: {
      xmlStr: null,
    },
    components: {},
    data() {
      return {
        containerEl: null,
        bpmnModeler: null,
        scale: 1,
        // 此变量为预览的xml文件数据，由于行数过多，附在了附件中，使用时，将附件整个复值到currentCanvasXml即可
        currentCanvasXml: null,
      };
    },
    computed: {},
    watch: {},
    methods: {
      handleZoom(flag) {
        if (flag < 0 && this.scale <= 1) {
          return;
        }
        this.scale += flag;
        this.$nextTick(() => {
          this.bpmnModeler.get('canvas').zoom('fit-viewport');
        });
      },

      previewTemp() {
        this.containerEl = document.getElementById('bpmn-canvas');
        // 避免缓存，每次清一下
        // this.bpmnModeler && this.bpmnModeler.destroy();
        this.scale = 1;
        this.bpmnModeler = new BpmnViewer({container: this.containerEl});
        const viewer = this.bpmnModeler;
        let xml = this.xmlStr || this.currentCanvasXml;
        if (xml) {
          this.bpmnModeler.importXML(xml, (err) => {
            if (err) {
              console.error(err);
            } else {
              // 只实现预览，核心代码以下两句足够
              const canvas = viewer.get('canvas');
              canvas.zoom('fit-viewport', 'auto');
              // 以下代码为：为节点注册鼠标悬浮事件
              const eventBus = this.bpmnModeler.get('eventBus');
              const overlays = this.bpmnModeler.get('overlays');
              eventBus.on('element.hover', (e) => {
                // const overlayHtml = this.$(` <div class="tipBox">
                //       你好，我是悬浮框里的内容
                //       </div>`);
                overlays.add(e.element.id, {
                  position: {top: e.element.height, left: 0},
                  html: '<div class="tipBox"></div>'
                  // html: overlayHtml
                });
              });
              eventBus.on('element.out', () => {
                overlays.clear();
              });
              //  注册悬浮事件结束
            }
          });
        }
      }
    },
    created() { //生命周期 - 创建完成（可以访问当前this实例）

    },
    mounted() { //生命周期 - 挂载完成（可以访问DOM元素）
      this.previewTemp();
    },
    activated() { //如果页面有keep-alive缓存功能，这个函数会触发

    },
  }
</script>
<style lang="scss" scoped>
  .bpmn-container {
    .tipBox {
      width: 200px;
      background: #fff;
      border-radius: 4px;
      border: 1px solid #ebeef5;
      padding: 12px;
    }
  }
</style>
