// @flow

import type { ProcessType } from '../datatypes/ProcessType'

class ProcessApi {
  getProcesses (): Promise<ProcessType[]> {
    const processDefault = {
      processTemplateId: 0,
      starterId: '',
      status: 'running',
      progress: 0,
      startedAt: '2019-12-18 12:00:00',
      tasks: []
    }
    const taskDefault = {
      templateDescription: '',
      taskTemplateId: 0,
      simpleClosing: true,
      personsResponsible: [],
      done: false
    }
    const proc1: ProcessType = {
      ...processDefault,
      id: 1,
      title: 'Process 1',
      progress: 0.75,
      tasks: [
        {
          ...taskDefault,
          taskId: 1,
          templateName: 'Task 1',
          done: true
        },
        {
          ...taskDefault,
          taskId: 2,
          templateName: 'Task 2',
          done: true
        },
        {
          ...taskDefault,
          taskId: 3,
          templateName: 'Task 3',
          done: true
        },
        {
          ...taskDefault,
          taskId: 4,
          templateName: 'Task 4'
        }
      ]
    }
    const proc2: ProcessType = {
      ...processDefault,
      id: 2,
      title: 'Process 2',
      progress: 0.5,
      tasks: [
        {
          ...taskDefault,
          taskId: 5,
          templateName: 'Task 5',
          done: true
        },
        {
          ...taskDefault,
          taskId: 6,
          templateName: 'Task 6'
        }
      ]
    }
    return Promise.resolve([proc1, proc2])
  }
}

export default ProcessApi
