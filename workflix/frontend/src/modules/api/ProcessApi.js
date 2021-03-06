// @flow

import { union, uniq } from 'lodash'
import type { ProcessTemplateMasterDataType, ProcessTemplateType, ProcessType } from '../datatypes/Process'
import type { TaskTemplateType } from '../datatypes/Task'
import { safeFetch } from './SafeFetch'
import type { FiltersType } from '../datatypes/Filters'
import { parseDatesInProcess, parseDatesInProcessTemplate } from './parseDates'
import type { NewIdResultType } from './common'
import { BACKEND } from './common'

const processesBackend = `${BACKEND}/processes`
const processesTemplatesBackend = `${BACKEND}/processTemplates`
const tasksBackend = `${BACKEND}/tasks`

export type FilledProcessTemplateType = {|
  title: string,
  description: string,
  durationLimit: number,
  ownerId: string,
  taskTemplates: TaskTemplateType[]
|}

export type ProcessConfigType = {|
  starterId: string, processGroupId: number, processTemplateId: number, title: string, description: string,
  deadline: Date
|}

class ProcessApi {
  getProcess (id: number): Promise<ProcessType> {
    return safeFetch(`${processesBackend}/${id}`)
      .then(response => response.json())
      .then(parseDatesInProcess)
  }

  getProcesses (filters: FiltersType = {}): Promise<ProcessType[]> {
    // convert filters into URL parameters
    const url = new URL(processesBackend)
    const params = union(
      filters.status ? filters.status.map(status => ['status', status]) : [],
      filters.involving ? [['involving', filters.involving.id]] : [],
      filters.processGroups ? filters.processGroups.map(group => ['processGroupId', group.id.toString()]) : []
    )
    url.search = new URLSearchParams(params).toString()

    // fetch the filtered processes
    return safeFetch(url)
      .then(response => response.json())
      .then(result => result.processes)
      .then(processes => Promise.all(processes.map(proc => this.getProcess(proc.id))))
  }

  startProcess (processConfig: ProcessConfigType): Promise<NewIdResultType> {
    return safeFetch(`${processesBackend}`, {
      method: 'POST',
      body: JSON.stringify({
        ...processConfig,
        deadline: processConfig.deadline.toISOString()
      })
    })
      .then(response => response.json())
  }

  addAssignee (taskId: number, assigneeId: string, immediateClosing: boolean = false): Promise<NewIdResultType> {
    return safeFetch(`${tasksBackend}/${taskId}/assignments/${assigneeId}`, {
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

  addProcessTemplate (processTemplate: FilledProcessTemplateType): Promise<NewIdResultType> {
    return safeFetch(`${processesTemplatesBackend}`, {
      method: 'POST',
      body: JSON.stringify(processTemplate)
    })
      .then(response => response.json())
  }

  editProcessTemplate (id: number, processTemplate: FilledProcessTemplateType): Promise<NewIdResultType> {
    return safeFetch(`${processesTemplatesBackend}/${id}`, {
      method: 'PATCH',
      body: JSON.stringify(processTemplate)
    })
      .then(response => response.json())
  }

  deleteProcessTemplate (id: number): Promise<Response> {
    return safeFetch(`${processesTemplatesBackend}/${id}`, { method: 'DELETE' })
  }

  getProcessTemplate (processTemplateId: number): Promise<ProcessTemplateType> {
    return safeFetch(`${processesTemplatesBackend}/${processTemplateId}`)
      .then(response => response.json())
      .then(parseDatesInProcessTemplate)
  }

  getAllProcessTemplates (): Promise<ProcessTemplateMasterDataType[]> {
    return safeFetch(`${processesTemplatesBackend}/`)
      .then(response => response.json())
      .then(json => json.templates.map(parseDatesInProcessTemplate))
  }

  getProcessTemplates (processTemplateIds: number[]): Promise<ProcessTemplateType[]> {
    processTemplateIds = uniq(processTemplateIds)
    return Promise.all(processTemplateIds.map(this.getProcessTemplate))
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

  addComment (taskId: number, creatorId: string, content: string): Promise<NewIdResultType> {
    return safeFetch(
      `${tasksBackend}/${taskId}/comments`,
      {
        method: 'POST',
        body: JSON.stringify({
          creatorId,
          content
        })
      }
    ).then(response => response.json())
  }

  abortProcess (processId: number): Promise<Response> {
    return safeFetch(`${processesBackend}/${processId}`, { method: 'DELETE' })
  }

  patchProcess (processId: number, title: string, description: string, deadline: Date): Promise<Response> {
    return safeFetch(`${processesBackend}/${processId}`, {
      method: 'PATCH',
      body: JSON.stringify({
        title,
        description,
        deadline: deadline.toISOString()
      })
    })
  }
}

export default ProcessApi
