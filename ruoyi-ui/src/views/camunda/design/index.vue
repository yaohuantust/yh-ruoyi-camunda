<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="KEY" prop="key">
        <el-input
          v-model="queryParams.key"
          placeholder="请输入KEY"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['activiti:modeler:add']"
        >创建新模型
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelerList">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="KEY" align="center" prop="key"/>
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="版本" align="center" prop="version"/>
      <!--      <el-table-column label="创建时间" align="center" prop="createTime" width="180"/>
            <el-table-column label="最后更新时间" align="center" prop="lastUpdateTime" width="180"/>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['activiti:modeler:edit']"
          >流程设计
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-upload2"
            @click="handleDeploy(scope.row)"
            v-hasPermi="['activiti:modeler:deploy']"
          >部署
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleExport(scope.row)"
            v-hasPermi="['activiti:modeler:export']"
          >导出
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['activiti:modeler:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改模型对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      fullscreen
      :destroy-on-close="true"
      class="status_change">
      <camunda-modeler
        v-if="open"
        :xmlStr="this.xmlString"
        @closeDialog="closeDesignDialog">
      </camunda-modeler>
    </el-dialog>

  </div>
</template>

<script>

  import CamundaModeler from "@/components/Camunda";
  import {listModeler, delModeler, getProcessDefinitionBpmn20Xml} from "@/api/camunda/modeler";
  import {format} from "@/utils/activiti/myUtil";

  export default {
    components: {CamundaModeler},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 模型表格数据
        modelerList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 日期范围
        dateRange: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          key: undefined,
          name: undefined,
        },
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单校验
        rules: {
          key: [
            {required: true, message: "KEY不能为空", trigger: "blur"}
          ],
          name: [
            {required: true, message: "名称不能为空", trigger: "blur"}
          ],
        },

        xmlString: "",
      };
    },
    computed: {},
    watch: {},
    methods: {
      getList() {
        this.loading = true;
        listModeler(this.queryParams).then(response => {
            this.modelerList = response.rows;
            this.modelerList.forEach(row => {
              row.createTime = format(row.createTime, 'yyyy-MM-dd HH:mm:ss');
              row.lastUpdateTime = format(row.lastUpdateTime, 'yyyy-MM-dd HH:mm:ss');
            });
            this.total = response.total;
            this.loading = false;
          }
        );
      },

      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          name: undefined,
          key: undefined,
          description: undefined,
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "流程设计";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id;
        const fileName = row.name;
        this.designModeler(id, fileName);
      },
      /** 提交按钮 */
      submitForm: function () {
      },
      /** 模型在线设计 */
      designModeler(id, fileName) {
        getProcessDefinitionBpmn20Xml(id, fileName).then(response => {
          if (response.code === 200) {
            this.xmlString = response.data.bpmn20Xml;
            console.log(this.xmlString);
            this.open = true;
          }
        })
      },

      /** 删除按钮操作 */
      handleDelete(row) {
        const modelIds = row.id;
        this.$confirm('是否确认删除ID为"' + modelIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delModeler(modelIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport(row) {
      },
      /** 部署按钮操作 */
      handleDeploy(row) {
      },


      closeDesignDialog(e) {
        console.log("代码已执行：" + e);
        this.open = false;
        this.getList();
      }

    },
    created() { //生命周期 - 创建完成（可以访问当前this实例）
      this.getList();
    },
    mounted() { //生命周期 - 挂载完成（可以访问DOM元素）

    },
    activated() { //如果页面有keep-alive缓存功能，这个函数会触发

    },
  }
</script>
<style lang="scss">
  .status_change {
    .el-dialog__title {
      font-weight: 600;
    }

    .el-dialog__body {
      padding: 0;
      height: calc(100% - 60px);
    }

  }

</style>
