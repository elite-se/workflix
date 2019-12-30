// @flow

import type { ProcessType } from '../datatypes/ProcessType'
import type { TaskType } from '../datatypes/TaskType'

const backend = 'https://wf-backend.herokuapp.com'
const processesBackend = `${backend}/processes`
const tasksBackend = `${backend}/tasks`

const patchJson = (input: RequestInfo, body: string) => {
  return fetch(input, {
    method: 'PATCH',
    mode: 'cors',
    headers: {
      'Content-Type': 'application/json'
    },
    body: body
  })
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

  patchAssignments (patchedTask: TaskType): Promise<Response> {
    return patchJson(`${tasksBackend}/${patchedTask.id}`, JSON.stringify(patchedTask.assignments))
  }
}

export default ProcessApi
