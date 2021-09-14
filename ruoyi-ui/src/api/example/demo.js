import request from '@/utils/request'

// 查询示例Demo列表
export function listDemo(query) {
  return request({
    url: '/example/demo/list',
    method: 'get',
    params: query
  })
}

// 查询示例Demo详细
export function getDemo(id) {
  return request({
    url: '/example/demo/' + id,
    method: 'get'
  })
}

// 新增示例Demo
export function addDemo(data) {
  return request({
    url: '/example/demo',
    method: 'post',
    data: data
  })
}

// 修改示例Demo
export function updateDemo(data) {
  return request({
    url: '/example/demo',
    method: 'put',
    data: data
  })
}

// 删除示例Demo
export function delDemo(id) {
  return request({
    url: '/example/demo/' + id,
    method: 'delete'
  })
}

// 导出示例Demo
export function exportDemo(query) {
  return request({
    url: '/example/demo/export',
    method: 'get',
    params: query
  })
}