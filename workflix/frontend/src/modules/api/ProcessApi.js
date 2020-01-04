// @flow

import { union, uniq } from 'lodash'
import type { ProcessTemplateType, ProcessType } from '../datatypes/Process'
import type { TaskTemplateType } from '../datatypes/Task'
import { safeFetch } from './SafeFetch'
import type { FiltersType } from '../datatypes/Filters'

const backend = 'https://wf-backend.herokuapp.com'
const processesBackend = `${backend}/processes`
const processesTemplatesBackend = `${backend}/processTemplates`
const tasksBackend = `${backend}/tasks`

const defaultFetchOptions = {
  headers: {
    'Content-Type': 'application/json'
  }
}

type NewIdResultType = {
  newId: number
}

class ProcessApi {
  getProcesses (filters: FiltersType = {}): Promise<ProcessType[]> {
    // convert filters into URL parameters
    const url = new URL(processesBackend)
    const params = union(
      filters.status ? filters.status.map(status => ['status', status]) : [],
      filters.involving ? [['involving', filters.involving.id]] : []
    )
    url.search = new URLSearchParams(params).toString()

    // fetch the filtered processes
    return safeFetch(url)
      .then(response => response.json())
      .then(result => result.processes)
      .then(processes => Promise.all(
        processes.map(proc =>
          safeFetch(`${processesBackend}/${proc.id}`)
            .then(response => response.json())
        )
      ))
      .then(processes => processes.map(process => ({
        ...process,
        startedAt: process.startedAt && new Date(process.startedAt)
      })))
  }

  addAssignee (taskId: number, assigneeId: string, immediateClosing: boolean = false): Promise<NewIdResultType> {
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
}

export default ProcessApi
