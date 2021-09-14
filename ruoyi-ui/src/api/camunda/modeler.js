// 查询模型列表
// import request from "@/utils/request";

import axios from 'axios'
import request from "@/utils/request";

// export function addModeler(data,config) {
//   return request({
//     url: '/camunda/modeler/create',
//     method: 'post',
//     params: data
//   })
// }

// 用户头像上传
export function addModeler(data) {
  return request({
    url: '/camunda/modeler/create',
    method: 'post',
    data: data
  })
}

// 查询模型列表
export function listModeler(data) {
  return request({
    url: '/camunda/modeler/list',
    method: 'post',
    params: data
  })
}


export function delModeler(id) {
  return request({
    url: '/camunda/modeler/remove/' + id,
    method: 'delete',
  })
}

// 获取xml
export function getProcessDefinitionBpmn20Xml(id, fileName) {
  return request({
    url: '/camunda/modeler/xml/' + id + '/' + fileName,
    method: 'get',
  })
}

// 获取xml
export function getProcessDefinitionBpmn20XmlByKey(key) {
  return request({
    url: '/camunda/modeler/xml/' + key,
    method: 'get',
  })
}

