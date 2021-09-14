<template>
  <div>
    <!--查看和审批对话框-->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="申请编号" prop="applyNum">
          <el-input v-model="form.applyNum" :readonly="taskName!=='调整申请'" />
        </el-form-item>
        <el-form-item label="是否高压停电">
          <el-radio-group v-model="form.highVoltage" :disabled="taskName!=='调整申请'">
            <el-radio label="0">否</el-radio>
            <el-radio label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="申请人" prop="applyUserName">
          <el-input v-model="form.applyUserName" :readonly="taskName!=='调整申请'" />
        </el-form-item>
        <el-form-item label="申请时间" prop="applyTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.applyTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择申请时间"
                          :readonly="taskName!=='调整申请'">
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
    name: "DemoForm",
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
        title: '查看示例Demo',
        form: this.row,
        open: false,
      }
    },
  }
</script>

<style scoped>

</style>
