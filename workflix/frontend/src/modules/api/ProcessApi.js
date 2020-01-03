// @flow

import { uniq } from 'lodash'
import type { ProcessTemplateType, ProcessType } from '../datatypes/Process'
import type { TaskTemplateType } from '../datatypes/Task'
import { safeFetch } from './SafeFetch'

const backend = 'https://wf-backend.herokuapp.com'
const processesBackend = `${backend}/processes`
const processesTemplatesBackend = `${backend}/processTemplates`
const tasksBackend = `${backend}/tasks`

const defaultFetchOptions = {
  headers: {
    'Content-Type': 'application/json'
  }
}

type AddAssigneeResultType = {
  newId: number
}

class ProcessApi {
  getProcesses (): Promise<ProcessType[]> {
    return safeFetch(processesBackend)
      .then(response => response.json())
      .then(result => result.processes)
      .then(processes => Promise.all(
        processes.map(proc =>
          safeFetch(`${processesBackend}/${proc.id}`)
            .then(response => response.json())
        )
      ))
  }

  addAssignee (taskId: number, assigneeId: string, immediateClosing: boolean = false): Promise<AddAssigneeResultType> {
    return safeFetch(`${tasksBackend}/${taskId}/assignments/${assigneeId}`, {
      ...defaultFetchOptions,
      method: 'PUT',
      body: JSON.stringify({
        immediateClosing
      })
    })
      .then(response => response.json())
  }

  removeAssignee (taskId: number, assigneeId: string): Promise<Response> {
    return safeFetch(
      `${tasksBackend}/${taskId}/assignments/${assigneeId}`,
      { method: 'DELETE' })
  }

  getProcessTemplate (processTemplateId: number): Promise<ProcessTemplateType> {
    return this.getProcessTemplates([processTemplateId])
      .then(templates => templates[0])
  }

  getProcessTemplates (processTemplateIds: number[]): Promise<ProcessTemplateType[]> {
    processTemplateIds = uniq(processTemplateIds)
    return Promise.all(processTemplateIds.map(procTempId =>
      safeFetch(`${processesTemplatesBackend}/${procTempId}`)
        .then(response => response.json())
    ))
  }

  getTaskTemplatesForProcessTemplates (processTemplateIds: number[]): Promise<Map<number, TaskTemplateType>> {
    return this.getProcessTemplates(processTemplateIds)
      .then(procTemps => procTemps.flatMap(procTemp => procTemp.taskTemplates))
      .then(taskTemps => new Map<number, TaskTemplateType>(taskTemps.map(taskTemp => [taskTemp.id, taskTemp])))
  }

  markAsDone (taskId: number, assigneeId: string): Promise<Response> {
    return safeFetch(
      `${tasksBackend}/${taskId}/assignments/${assigneeId}`,
      { method: 'PATCH' })
  }
}

export default ProcessApi
