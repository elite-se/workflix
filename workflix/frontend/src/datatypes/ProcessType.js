// @flow

import type { TaskTemplateType, TaskType } from './TaskType'

export type ProcessStatusType = 'running' | 'closed' | 'aborted'

export type ProcessTemplateType = {
  id: number,
  title: string,
  description: string,
  durationLimit: number,
  ownerId: string,
  createdAt: string,
  process_count: number,
  running_processes: number,
  deleted: boolean,
  taskTemplates: TaskTemplateType[]
}

export type ProcessType = {
  id: number,
  title: string,
  description: string,
  processTemplateId: number,
  starterId: string,
  status: ProcessStatusType,
  progress: number,
  startedAt: string,
  tasks: TaskType[]
}
