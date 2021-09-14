<template>
  <div>
    <!--以下是审批字段-->
    <div v-show="type === 'todo'">
      <el-form label-width="100px">
        <el-divider></el-divider>
        <el-form-item label="审批意见">
          <el-radio-group v-model="pass">
            <el-radio label="true">同意</el-radio>
            <el-radio label="false">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="批注" prop="remark">
          <el-input v-model="comment" type="textarea" :value="comment" />
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer" style="text-align: right;">
      <el-button v-show="type === 'todo'" type="primary" @click="complete">确 定</el-button>
      <el-button @click="close">关 闭</el-button>
    </div>
  </div>
</template>

<script>
  import request from '@/utils/request'

  export default {
    name: "ViewVerify",
    props: {
      row: {
        type: Object,
      },
      taskId: {
        type: String,
      },
      type: {
        type: String,
      },
    },
    data() {
      return {
        pass: 'true',
        comment: '同意',
      }
    },
    methods: {
      close: function () {
        this.$emit('update:open', false);
      },
      complete() {
        this.$parent.$children[0].validate(valid => {
          if (valid) {
            const data = {
              "taskId": this.taskId,
              "instanceId": this.row.instanceId,
              "variables": JSON.stringify({
                "comment": this.comment,
                "pass": this.pass,
                "formData": this.row,
              }),
            };
            return request({
              url: '/activiti/process/complete',
              method: 'post',
              params: data
            }).then(response => {
              this.close();
              // 刷新待办事项列表（丑陋的代码 👇）
              this.$parent.$parent.$parent.$parent.$parent.$parent.getList();
              this.msgSuccess("操作成功");
            });
          }
        });
      },
    },
    watch: {
      pass: function(val) {
        this.comment = val === 'true' ? '同意' : '驳回';
      },
    },
  }
</script>

<style scoped>

</style>
