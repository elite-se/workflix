// @flow

import type { TaskTemplateType, TaskType } from './Task'
import type { UserType } from './User'

export type ProcessStatusType = 'RUNNING' | 'CLOSED' | 'ABORTED'

export type ProcessTemplateMasterDataType = {|
  id: number,
  title: string,
  durationLimit: ?number,
  owner: UserType
|}

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
