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
  assigneeId: string,
  status: TaskAssignmentStatusType,
  createdAt: string,
  doneAt: ?string
}

export type TaskStatusType = 'BLOCKED' | 'RUNNING' | 'CLOSED'

export type TaskType = {
  taskId: number,
  templateName: string,
  templateDescription: string,
  taskTemplateId: number,
  comments: TaskCommentType[],
  assignments: TaskAssignmentType[],
  status: TaskStatusType
}
