// @flow

import type { TaskType } from './TaskType'

export type ProcessStatusType = 'running' | 'closed' | 'aborted'

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
