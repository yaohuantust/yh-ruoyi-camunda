<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="申请编号" prop="applyNum">
        <el-input
          v-model="queryParams.applyNum"
          placeholder="请输入申请编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="高压停电" prop="highVoltage">
        <el-select v-model="queryParams.highVoltage" placeholder="请选择是否高压停电" clearable size="small">
          <el-option label="否" value="0" />
          <el-option label="是" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="申请人" prop="applyUserName">
        <el-input
          v-model="queryParams.applyUserName"
          placeholder="请输入申请人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请时间" prop="applyTime">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.applyTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择申请时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="流程实例" prop="instanceId">
        <el-input
          v-model="queryParams.instanceId"
          placeholder="请输入流程实例ID"
          clearable
          size="small"
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
          v-hasPermi="['example:demo:add']"
        >新增</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="demoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="60" />
      <el-table-column label="申请编号" align="center" prop="applyNum" width="120" />
      <el-table-column label="是否高压停电" align="center" prop="highVoltage" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.highVoltage === '0' ? '否' : '是' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请人" align="center" prop="applyUserName" width="90" />
      <el-table-column label="申请时间" align="center" prop="applyTime" width="180" />
      <el-table-column label="流程实例ID" align="center" prop="instanceId" width="100" />
      <el-table-column label="备注" align="center" prop="remark" width="180" />
      <el-table-column label="当前任务名称" align="center" prop="taskName" width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--本案例提交申请前有做会签参与人选择的操作，因此不引入 ApplyBefore 子组件-->
          <el-button
            v-show="!scope.row.instanceId"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleApplyView(scope.row)"
          >提交申请</el-button>
          <el-button
            v-show="!scope.row.instanceId"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-show="!scope.row.instanceId"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
          <apply-after
            v-show="scope.row.instanceId"
            :row="scope.row"
            :taskId="scope.row.taskId"
            :type="scope.row.type"
            @getList="getList"
          ></apply-after>
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

    <!-- 添加或修改示例Demo对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="申请编号" prop="applyNum">
          <el-input v-model="form.applyNum" placeholder="请输入申请编号" />
        </el-form-item>
        <el-form-item label="是否高压停电">
          <el-radio-group v-model="form.highVoltage">
            <el-radio label="0">否</el-radio>
            <el-radio label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--选择会签参与人对话框-->
    <el-dialog title="选择调度会会签参与人" :visible.sync="showSelectUser" width="400px" append-to-body>
      <el-form ref="selectUserForm" :rules="selectUserRules" label-width="80px">
        <el-form-item label="参与人">
          <el-select v-model="userNames" multiple placeholder="请选择">
            <el-option
              v-for="item in userOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userName"
              :disabled="item.status === 1"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleApply">确 定</el-button>
        <el-button @click="userNames=[];showSelectUser=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDemo, getDemo, delDemo, addDemo, updateDemo, exportDemo } from "@/api/example/demo";
import ApplyBefore from "@/components/Activiti/ApplyBefore/index";
import ApplyAfter from "@/components/Activiti/ApplyAfter/index";
import { listUser } from "@/api/system/user";
import request from '@/utils/request'

export default {
  name: "Demo",
  components: {
    ApplyBefore,
    ApplyAfter,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 示例Demo表格数据
      demoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        applyNum: null,
        highVoltage: null,
        attachment: null,
        applyUserId: null,
        applyUserName: null,
        applyTime: null,
        instanceId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        applyNum: [
          { required: true, message: "申请编号不能为空", trigger: "blur" }
        ],
        highVoltage: [
          { required: true, message: "是否高压停电不能为空", trigger: "blur" }
        ],
      },
      requestMapping: '/example/demo',

      selectedRow: {},
      selectUserForm: {},
      showSelectUser: false,
      selectUserRules: {
        userOptions: [
          { required: true, message: '参与人不能为空', trigger: 'blur' },
        ],
      },
      userOptions: [],
      userNames: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询示例Demo列表 */
    getList() {
      this.loading = true;
      listDemo(this.queryParams).then(response => {
        this.demoList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).then(() => {
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        applyNum: null,
        highVoltage: "0",
        attachment: null,
        applyUserId: null,
        applyUserName: null,
        applyTime: null,
        instanceId: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加示例Demo";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDemo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改示例Demo";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDemo(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDemo(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除示例Demo编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDemo(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有示例Demo数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportDemo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    handleApply: function () {
      if (this.userNames.length === 0) {
        this.msgError('请先选择会签参与人');
        return;
      }
      const selectedRow = this.selectedRow;
      const requestMapping = this.requestMapping;
      const userNames = this.userNames.toString();
      this.$confirm('是否提交ID为"' + selectedRow.id + '"的申请单据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return request({
          url: requestMapping + '/submitApply/' + selectedRow.id,
          method: 'post',
          params: { "variablesStr": { "gytd": selectedRow.highVoltage, "users": userNames } },
        });
      }).then(() => {
        this.getList();
        this.msgSuccess("申请成功");
        this.userNames = [];
        this.showSelectUser = false;
      })
    },

    handleApplyView(row) {
      this.selectedRow = row;
      this.showSelectUser = true;
      this.getUserList();
    },

    /** 查询用户列表 */
    getUserList() {
      listUser().then(response => {
          this.userOptions = response.rows;
        }
      );
    },

  }
};
</script>
