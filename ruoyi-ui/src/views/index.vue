<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12">
        <blockquote class="text-primary" style="font-size: 14px">
          <h4>
          主要功能：<br />
          1. 流程模型设计、部署、导出、删除；<br />
          2. 在线流程设计支持用户、候选人、候选组弹窗选择；<br />
          3. 流程定义部署、挂起/激活、转模型、删除；<br />
          4. 提供请假业务、示例Demo两条流程；<br />
          5. 业务表单常规增删改查、支持提交申请、表单查看、审批历史、进度查看、挂起/激活、撤销；<br />
          6. 聚合显示我的待办、我的已办；<br />
          7. 我的待办支持办理、转办、审批历史、进度查看；<br />
          8. 我的已办支持表单查看、审批历史、进度查看。<br /><br />

          代码生成 CRUD + 流程设计 + 闲鹿工作流二次开发规范，业务表单绑定流程 SO EASY。<br />
          更多功能正在开发中……
          </h4>

        </blockquote>

        <hr />
      </el-col>
      <el-col :sm="24" :lg="12">
        <h4 class="text-danger" style="font-size: 14px">
          Test Case:<br /><br />
          一、示例Demo<br />
          申请人：任意用户<br />
          机电科电管员：axianlu / 123456<br />
          供电管理口领导：rensm / 12345<br />
          调度会会签：根据提交前所选用户<br /><br />

          二、请假业务<br />
          普通员工：chengxy / 123456<br />
          部门领导：axianlu / 123456<br />
          人事：rensm / 12345<br /><br />

          注：除了 admin 和 ry 外，所有用户密码均为 123456（详见用户管理）
        </h4>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-user-solid"></i> QQ群：603909590
            </p>
            <p>
              <i class="el-icon-chat-dot-round"></i> 微信：<a
              href="javascript:;"
            >huangluqian12345</a
            >
            </p>
            <p>
              <i class="el-icon-user-solid"></i> QQ：<a
              href="javascript:;"
            >1055202292</a
            >
            </p>
            <img
              src="~@/assets/image/qqgroup.jpg"
              alt="qqgroup"
              width="100%"
            />
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>更新日志</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item title="v1.0.0 - 2020-12-16">
              <ol>
                <li>闲鹿工作流分离版正式发布</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>捐赠支持</span>
          </div>
          <div class="body">
            <img
              src="~@/assets/image/pay.png"
              alt="donate"
              width="100%"
            />
            <span style="display: inline-block; height: 30px; line-height: 30px"
              >你可以请作者喝杯咖啡表示鼓励</span
            >
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      // 版本号
      version: "3.2.1",
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

