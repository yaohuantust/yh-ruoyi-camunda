import request from '@/utils/request'

// 查询请假列表
export function listLeave(query) {
  return request({
    url: '/leave/leave/list',
    method: 'get',
    params: query
  })
}

// 查询请假详细
export function getLeave(id) {
  return request({
    url: '/leave/leave/' + id,
    method: 'get'
  })
}

// 新增请假
export function addLeave(data) {
  return request({
    url: '/leave/leave',
    method: 'post',
    data: data
  })
}

// 修改请假
export function updateLeave(data) {
  return request({
    url: '/leave/leave',
    method: 'put',
    data: data
  })
}

// 删除请假
export function delLeave(id) {
  return request({
    url: '/leave/leave/' + id,
    method: 'delete'
  })
}

// 导出请假
export function exportLeave(query) {
  return request({
    url: '/leave/leave/export',
    method: 'get',
    params: query
  })
}