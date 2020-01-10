// @flow

import type { TaskTemplateType, TaskType } from './Task'

export type ProcessStatusType = 'RUNNING' | 'CLOSED' | 'ABORTED'

export type ProcessTemplateMasterDataType = {
  id: number,
  title: string,
  description: string,
  durationLimit: number,
  createdAt: Date,
  processCount: number,
  runningProcesses: number,
  deleted: boolean,
  ownerId: string
}

export type ProcessTemplateType = {|
  id: number,
  title: string,
  description: string,
  durationLimit: number,
  ownerId: string,
  createdAt: Date,
  processCount: number,
  runningProcesses: number,
  deleted: boolean,
  taskTemplates: TaskTemplateType[]
|}

export type ProcessType = {|
  id: number,
  title: string,
  description: string,
  processTemplateId: number,
  processGroupId: number,
  starterId: string,
  status: ProcessStatusType,
  progress: number,
  startedAt: Date,
  deadline: Date,
  tasks: TaskType[]
|}

export const statusTranslation = {
  ABORTED: 'aborted',
  CLOSED: 'done',
  RUNNING: 'running'
}
