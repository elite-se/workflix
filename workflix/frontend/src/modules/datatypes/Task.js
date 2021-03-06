// @flow

export type TaskCommentType = {
  id: number,
  creatorId: string,
  content: string,
  createdAt: Date
}

export type TaskStatusType = 'BLOCKED' | 'RUNNING' | 'CLOSED'

export type TaskAssignmentType = {|
  id: ?number,
  assigneeId: string,
  createdAt: ?Date,
  doneAt: ?Date,
  closed: boolean
|}

export type TaskTemplateType = {|
  id: number,
  name: string,
  description: string,
  estimatedDuration: number,
  necessaryClosings: number,
  responsibleUserRoleId: number,
  predecessors: number[]
|}

export type TaskType = {|
  id: number,
  taskTemplateId: number,
  status: TaskStatusType,
  comments: TaskCommentType[],
  assignments: TaskAssignmentType[]
|}
