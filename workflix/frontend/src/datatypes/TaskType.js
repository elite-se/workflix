// @flow

export type TaskCommentType = {
  id: number,
  creatorId: string,
  title: string,
  content: string,
  createdAt: string
}

export type TaskAssignmentStatusType = 'TODO' | 'RUNNING' | 'CLOSED'

export type TaskAssignmentType = {
  id: number,
  assigneeId: string,
  status: TaskAssignmentStatusType,
  createdAt: string,
  doneAt: ?string
}

export type TaskTemplateType = {
  id: number,
  name: string,
  description: string,
  estimatedDuration: number,
  durationLimit: number,
  necessaryClosings: number,
  predecessors: number[]
}

export type TaskType = {
  id: number,
  taskTemplateId: number,
  comments: TaskCommentType[],
  assignments: TaskAssignmentType[],
  startedAt: string,
  done: boolean,
  taskTemplate: TaskTemplateType
}
