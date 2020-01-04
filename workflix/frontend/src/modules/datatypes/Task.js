// @flow

export type TaskCommentType = {
  id: number,
  creatorId: string,
  content: string,
  createdAt: Date
}

export type TaskStateType = 'BLOCKED' | 'RUNNING' | 'CLOSED'

export type TaskAssignmentType = {|
  id: ?number,
  assigneeId: string,
  createdAt: ?Date,
  doneAt: ?string,
  closed: boolean
|}

export type TaskTemplateType = {|
  id: number,
  name: string,
  description: string,
  estimatedDuration: number,
  necessaryClosings: number,
  predecessors: number[]
|}

export type TaskType = {|
  id: number,
  taskTemplateId: number,
  status: TaskStateType,
  comments: TaskCommentType[],
  assignments: TaskAssignmentType[]
|}
