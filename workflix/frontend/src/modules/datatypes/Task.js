// @flow

export type TaskCommentType = {
  id: number,
  creatorId: string,
  content: string,
  createdAt: string
}

export type TaskStateType = 'BLOCKED' | 'RUNNING' | 'CLOSED'

export type TaskAssignmentType = {|
  id: ?number,
  assigneeId: string,
  createdAt: ?string,
  doneAt: ?string,
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
  status: TaskStateType,
  comments: TaskCommentType[],
  assignments: TaskAssignmentType[]
|}
