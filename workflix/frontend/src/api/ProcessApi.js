// @flow

import type { ProcessType } from 'datatypes/ProcessType'

class ProcessApi {
  getProcesses (): ProcessType[] {
    return [{
      masterData: {
        id: 1,
        title: 'Process 1'
      },
      tasks: [
        {
          id: 1,
          name: 'Task 1',
          finished: true
        },
        {
          id: 2,
          name: 'Task 2',
          finished: true
        },
        {
          id: 3,
          name: 'Task 3',
          finished: true
        },
        {
          id: 4,
          name: 'Task 4',
          finished: false
        }
      ]
    }, {
      masterData: {
        id: 2,
        title: 'Process 2'
      },
      tasks: [
        {
          id: 5,
          name: 'Task 5',
          finished: true
        },
        {
          id: 6,
          name: 'Task 6',
          finished: false
        }
      ]
    }]
  }
}

export default ProcessApi
