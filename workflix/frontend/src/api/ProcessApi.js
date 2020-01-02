// @flow

import type { ProcessType } from '../datatypes/ProcessType'

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
}

export default ProcessApi
