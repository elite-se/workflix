// @flow

import type { ProcessType } from '../datatypes/ProcessType'
import type { TaskType } from '../datatypes/TaskType'

const backend = 'https://wf-backend.herokuapp.com'
const processesBackend = `${backend}/processes`

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

  patchAssignments (patchedTask: TaskType) {
    this.procs = this.procs.map(proc => {
      proc.tasks = proc.tasks.map((task: TaskType) => {
        if (task.taskId === patchedTask.taskId) {
          task.assignments = patchedTask.assignments
        }
        return { ...task, assignments: task.assignments }
      })
      return { ...proc, tasks: proc.tasks }
    })
  }
}

export default ProcessApi
