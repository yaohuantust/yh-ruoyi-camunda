<template>
  <div>
    <!--查看和审批对话框-->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="请假类型" prop="type">
          <el-select v-model="form.type" :disabled="taskName!=='调整申请'">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" :readonly="taskName!=='调整申请'" />
        </el-form-item>
        <el-form-item label="原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" :readonly="taskName!=='调整申请'" />
        </el-form-item>
        <el-form-item label="开始时间" prop="leaveStartTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.leaveStartTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          :readonly="taskName!=='调整申请'">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="leaveEndTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.leaveEndTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          :readonly="taskName!=='调整申请'">
          </el-date-picker>
        </el-form-item>
        <!--<el-form-item label="请假时长" prop="totalTime">
          <el-input v-model="form.totalTime" readonly />
        </el-form-item>-->
        <el-form-item label="实际开始时间" prop="realityStartTime" v-if="taskName==='销假' || type !== 'todo'">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.realityStartTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择实际开始时间"
                          :readonly="taskName!=='销假'">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实际结束时间" prop="realityEndTime" v-if="taskName==='销假' || type !== 'todo'">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.realityEndTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择实际结束时间"
                          :readonly="taskName!=='销假'">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="申请人" prop="applyUserName">
          <el-input v-model="form.applyUserName" readonly />
        </el-form-item>
        <el-form-item label="申请时间" prop="applyTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.applyTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          readonly>
          </el-date-picker>
        </el-form-item>
        <!--<el-form-item label="流程实例ID" prop="instanceId">
          <el-input v-model="form.instanceId" readonly />
        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :readonly="taskName!=='调整申请'" />
        </el-form-item>
      </el-form>
      <view-verify
        :open.sync="open"
        :row="row"
        :taskId="taskId"
        :type="type"
      />
    </el-dialog>
  </div>
</template>

<script>
  import ViewVerify from "@/components/Activiti/ViewVerify/index";

  export default {
    name: "LeaveForm",
    components: {
      ViewVerify,
    },
    props: {
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
        title: '查看请假表单',
        form: this.row,
        open: false,
        // 请假类型字典
        typeOptions: [],
        // 表单校验
        rules: {
          type: [
            { required: true, message: "请假类型不能为空", trigger: "blur" },
          ],
          title: [
            { required: true, message: "标题不能为空", trigger: "blur" },
          ],
          reason: [
            { required: true, message: "原因不能为空", trigger: "blur" },
          ],
          leaveStartTime: [
            { required: true, message: "开始时间不能为空", trigger: "blur" },
          ],
          leaveEndTime: [
            { required: true, message: "结束时间不能为空", trigger: "blur" },
          ],
          realityStartTime: [
            { required: true, message: "实际开始时间不能为空", trigger: "blur" },
          ],
          realityEndTime: [
            { required: true, message: "实际结束时间不能为空", trigger: "blur" },
          ],
        },
      }
    },
    created() {
      this.getDicts("biz_leave_type").then(response => {
        this.typeOptions = response.data;
      });
    },
  }
</script>

<style scoped>

</style>
