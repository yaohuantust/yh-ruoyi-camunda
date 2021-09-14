<template>
  <div>
    <el-button
      size="mini"
      type="text"
      :icon="icon"
      @click="_handleView"
    >{{ type === 'todo' ? '办理' : '表单数据' }}</el-button>
    <el-button
      size="mini"
      type="text"
      icon="el-icon-user"
      @click="handleViewUserList"
      v-show="type === 'todo'"
    >转办</el-button>
    <el-button
      size="mini"
      type="text"
      icon="el-icon-time"
      @click="handleViewHistoryList"
      v-show="row.taskId !== '-2'"
    >审批历史</el-button>
    <el-button
      size="mini"
      type="text"
      icon="el-icon-search"
      @click="handleViewProcessImg"
      v-show="row.taskId !== '-2'"
    >进度查看</el-button>
    <el-button
      size="mini"
      type="text"
      icon="el-icon-back"
      @click="handleCancel"
      v-show="row.taskId !== '-1' && row.taskId !== '-2' && type === 'none'"
    >撤销</el-button>
    <el-button
      size="mini"
      type="text"
      icon="el-icon-sort"
      @click="handleState"
      v-show="row.taskId !== '-1' && row.taskId !== '-2' && type === 'none'"
    >{{ row.suspendState === '2' ? '激活' : '挂起' }}</el-button>

    <!--业务表单子组件-->
    <demo-form
      v-if="row.processKey === 'exampleDemo'"
      :row="row"
      :taskId="taskId"
      :taskName="taskName"
      :type="type"
      ref="demoForm"
    ></demo-form>
    <leave-form
      v-if="row.processKey === 'leave'"
      :row="row"
      :taskId="taskId"
      :taskName="taskName"
      :type="type"
      ref="leaveForm"
    ></leave-form>

    <!--进度查看对话框-->
    <el-dialog title="进度查看" :visible.sync="open" width="1000px" append-to-body>
      <div style="width: 100%; text-align: center;">
        <el-image
          :src="src"
          :preview-src-list="srcList">
        </el-image>
      </div>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="close">关闭</el-button>
      </div>
    </el-dialog>

    <!--审批历史对话框-->
    <el-dialog title="审批历史" :visible.sync="showHistoryTable" width="1000px" append-to-body>
      <!--审批历史表格-->
      <div>
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="70px">
          <el-form-item label="任务名称" prop="activityName">
            <el-input
              v-model="queryParams.activityName"
              placeholder="请输入任务名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="办理人ID" prop="assignee">
            <el-input
              v-model="queryParams.assignee"
              placeholder="请输入办理人ID"
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
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getHistoryList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="historyList">
          <el-table-column v-if="false" label="活动ID" align="center" prop="activityId" width="60" />
          <el-table-column label="活动名称" align="center" prop="activityName" width="140" />
          <el-table-column label="办理人ID" align="center" prop="assignee" width="90" />
          <el-table-column label="办理人" align="center" prop="assigneeName" width="80" />
          <el-table-column label="审批意见" align="center" prop="comment" />
          <el-table-column label="开始时间" align="center" prop="startTime" width="160" />
          <el-table-column label="结束时间" align="center" prop="endTime" width="160" />
          <el-table-column label="耗时" align="center" prop="durationInMillis" width="150" />
        </el-table>

        <!--v-show="total>0"-->
        <pagination
          v-show="false"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getHistoryList"
        />
      </div>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button @click="showHistoryTable=false">关闭</el-button>
      </div>
    </el-dialog>

    <!--用户列表对话框-->
    <el-dialog title="选择用户" :visible.sync="showUserTable" width="900px" append-to-body>
      <!--用户列表表格-->
      <div>
        <el-form :model="queryUserParams" ref="queryUserForm" :inline="true" v-show="showUserSearch" label-width="68px">
          <el-form-item label="用户名称" prop="userName">
            <el-input
              v-model="queryUserParams.userName"
              placeholder="请输入用户名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleUserQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              v-model="queryUserParams.phonenumber"
              placeholder="请输入手机号码"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleUserQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleUserQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetUserQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <right-toolbar :showSearch.sync="showUserSearch" @queryTable="getUserList"></right-toolbar>
        </el-row>

        <el-table v-loading="userLoading" :data="userList" @selection-change="handleUserSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="用户编号" align="center" prop="userId" />
          <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true" />
          <el-table-column label="用户昵称" align="center" prop="nickName" :show-overflow-tooltip="true" />
          <el-table-column label="部门" align="center" prop="dept.deptName" :show-overflow-tooltip="true" />
          <el-table-column label="手机号码" align="center" prop="phonenumber" width="120" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="userTotal>0"
          :total="userTotal"
          :page.sync="queryUserParams.pageNum"
          :limit.sync="queryUserParams.pageSize"
          @pagination="getUserList"
        />
      </div>
      <div slot="footer" class="dialog-footer" style="text-align: right;">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="showUserTable=false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import request from '@/utils/request'
  import { format, formatTotalDateSub } from "@/utils/activiti/myUtil.js"
  import { listUser } from "@/api/system/user";
  import DemoForm from "@/views/example/demo/demoForm";
  import LeaveForm from "@/views/leave/leave/leaveForm";

  export default {
    name: "ApplyAfter",
    components: {DemoForm, LeaveForm},
    props: {
      /* 表格行数据 */
      row: {
        type: Object,
      },
      taskId: {
        type: String,
      },
      taskName: {
        type: String,
      },
      type: {
        type: String,
        default: 'none',
      },
    },
    data() {
      return {
        open: false,
        src: '',
        srcList: [],

        showHistoryTable: false,
        // 遮罩层
        loading: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 999,
          processInstanceId: null,
          activityName: null,
          assignee: null,
        },
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        historyList: [],
        icon: this.type === 'todo' ? 'el-icon-check' : 'el-icon-document',
        // 选中行
        selectedRow: null,
        // 状态数据字典
        statusOptions: [],
        showUserTable: false,
        // 遮罩层
        userLoading: false,
        // 查询参数
        queryUserParams: {
          pageNum: 1,
          pageSize: 10,
          userName: undefined,
          phonenumber: undefined,
          status: undefined,
        },
        // 显示搜索条件
        showUserSearch: true,
        // 总条数
        userTotal: 0,
        userList: [],
      }
    },
    create() {
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      _handleView: function () {
        switch (this.row.processKey) {
          case 'exampleDemo':
            this.$refs.demoForm.form = this.row;
            this.$refs.demoForm.open = true;
            break;
          case 'leave':
            this.$refs.leaveForm.form = this.row;
            this.$refs.leaveForm.open = true;
            break;
          default:
            break;
        }
      },
      handleState: function () {
        const instanceId = this.row.instanceId;
        const suspendState = this.row.suspendState;
        const opt = this.row.suspendState === '2' ? '激活': '挂起';
        this.$confirm('是否确认' + opt + 'ID为"' + instanceId + '"的流程实例?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          const data = { "instanceId": instanceId, "suspendState": suspendState };
          return request({
            url: '/activiti/process/suspendOrActiveApply',
            method: 'post',
            params: data
          });
        }).then(response => {
          this.$emit('getList');
          this.msgSuccess("操作成功");
        }).catch(function () {
        });
      },
      handleCancel: function () {
        const instanceId = this.row.instanceId;
        this.$confirm('是否确认撤销ID为"' + instanceId + '"的流程实例?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          const data = { "instanceId": instanceId };
          return request({
            url: '/activiti/process/cancelApply',
            method: 'post',
            params: data
          });
        }).then(response => {
          this.$emit('getList');
          this.msgSuccess("操作成功");
        }).catch(function () {
        });
      },
      handleViewProcessImg: function() {
        const baseURL = process.env.VUE_APP_BASE_API;
        this.src = baseURL + '/activiti/process/read-resource?pProcessInstanceId=' + this.row.instanceId + '&_=' + new Date().getTime();
        this.srcList = [];
        this.srcList.push(this.src);
        this.open = true;
      },
      close: function () {
        this.open = false;
      },
      handleViewHistoryList() {
        this.queryParams.processInstanceId = this.row.instanceId;
        this.showHistoryTable = true;
        this.getHistoryList();
      },
      getHistoryList: function () {
        this.loading = true;
        return request({
          url: '/activiti/process/listHistory',
          method: 'post',
          data: this.queryParams
        }).then(response => {
          this.historyList = response.rows;
          this.historyList.forEach(row => {
            row.startTime = format(row.startTime, 'yyyy-MM-dd HH:mm:ss');
            row.endTime = format(row.endTime, 'yyyy-MM-dd HH:mm:ss');
            row.durationInMillis = formatTotalDateSub(row.durationInMillis / 1000);
          });
          this.total = response.total;
          this.loading = false;
        }).then(() => {
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getHistoryList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      handleViewUserList() {
        this.showUserTable = true;
        this.getUserList();
      },
      /** 查询用户列表 */
      getUserList() {
        this.loading = true;
        listUser(this.queryUserParams).then(response => {
            this.userList = response.rows;
            this.userTotal = response.total;
            this.userLoading = false;
          }
        );
      },
      /** 搜索按钮操作 */
      handleUserQuery() {
        this.queryUserParams.page = 1;
        this.getUserList();
      },
      /** 重置按钮操作 */
      resetUserQuery() {
        this.resetForm("queryUserForm");
        this.handleUserQuery();
      },
      // 多选框选中数据
      handleUserSelectionChange(selection) {
        this.selectedRow = selection[0];
      },
      /** 提交按钮 */
      submitForm() {
        const taskId = this.taskId;
        const selectedRow = this.selectedRow;
        if (!selectedRow) {
          this.msgError('请先选择要转办的用户');
          return;
        }
        if (selectedRow.userName === this.$store.state.user.name) {
          this.msgError('不能转办给自己');
          return;
        }
        this.$confirm('是否确认转办给' + selectedRow.nickName + '?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return request({
            url: '/activiti/process/delegate',
            method: 'post',
            params: { "taskId": taskId, "delegateToUser": selectedRow.userName },
          });
        }).then(() => {
          this.$emit('getList');
          this.msgSuccess("操作成功");
          this.showUserTable = false;
        })
      },
    },
  }
</script>

<style scoped>

</style>
