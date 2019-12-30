// @flow

import type { ProcessType } from '../datatypes/ProcessType'

const backend = 'https://wf-backend.herokuapp.com'
const processesBackend = `${backend}/processes`
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
    return fetch(processesBackend)
      .then(response => response.json())
      .then(result => result.processes)
      .then(processes => Promise.all(
        processes.map(proc =>
          fetch(`${processesBackend}/${proc.id}`)
            .then(response => response.json())
        )
      ))
  }

  addAssignee (taskId: number, assigneeId: string, immediateClosing: boolean = false): Promise<AddAssigneeResultType> {
    return fetch(`${tasksBackend}/${taskId}/assignments/${assigneeId}`, {
      ...defaultFetchOptions,
      method: 'PUT',
      body: JSON.stringify({
        immediateClosing: immediateClosing
      })
    })
      .then(response => response.json())
  }

  removeAssignee (taskId: number, assigneeId: string): Promise<Response> {
    return fetch(
      `${tasksBackend}/${taskId}/assignments/${assigneeId}`,
      { method: 'DELETE' })
  }

  markAsDone (taskId: number, assigneeId: string): Promise<Response> {
    return fetch(
      `${tasksBackend}/${taskId}/assignments/${assigneeId}`,
      { method: 'PATCH' })
  }
}

export default ProcessApi
