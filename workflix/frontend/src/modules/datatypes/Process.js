// @flow

import type { TaskTemplateType, TaskType } from './Task'

export type ProcessStatusType = 'RUNNING' | 'CLOSED' | 'ABORTED'

export type ProcessTemplateMasterDataType = {|
  id: number,
  title: string,
  description: string,
  durationLimit: ?number,
  deleted: boolean,
  ownerId: string
|}

export type ProcessTemplateType = {|
  id: number,
  title: string,
  description: string,
  durationLimit: number,
  ownerId: string,
  createdAt: Date,
  process_count: number,
  running_processes: number,
  deleted: boolean,
  taskTemplates: TaskTemplateType[]
|}

export type ProcessType = {|
  id: number,
  title: string,
  description: string,
  processTemplateId: number,
  starterId: string,
  status: ProcessStatusType,
  progress: number,
  startedAt: Date,
  tasks: TaskType[]
|}
