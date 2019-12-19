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
      simpleClosing: true,
      personsResponsible: [],
      done: false
    }
    const proc1: ProcessType = {
      ...processDefault,
      id: 13,
      title: 'Kapitalabruf BMW-Aktie',
      progress: 0.4,
      tasks: [
        {
          ...taskDefault,
          taskId: 1,
          templateName: 'Dokument scannen',
          taskTemplateId: 1,
          done: true
        },
        {
          ...taskDefault,
          taskId: 2,
          templateName: 'Prüfen (Admin)',
          taskTemplateId: 2,
          done: true
        },
        {
          ...taskDefault,
          taskId: 3,
          templateName: 'Prüfen (Asset Manager)',
          taskTemplateId: 3
        },
        {
          ...taskDefault,
          taskId: 4,
          templateName: 'Zahlung veranlassen',
          taskTemplateId: 4
        },
        {
          ...taskDefault,
          taskId: 5,
          templateName: 'Buchung eintragen',
          taskTemplateId: 5
        }
      ]
    }
    const proc2: ProcessType = {
      ...processDefault,
      id: 2,
      title: 'Dummy-Task',
      progress: 0.5,
      tasks: [
        {
          ...taskDefault,
          taskId: 11,
          templateName: 'Ignore this task',
          taskTemplateId: 6,
          done: true
        },
        {
          ...taskDefault,
          taskId: 12,
          templateName: 'Ignore this one, too',
          taskTemplateId: 7
        }
      ]
    }
    const proc3: ProcessType = {
      ...processDefault,
      id: 15,
      title: 'Kapitalabruf Audi-Aktie',
      progress: 0.2,
      tasks: [
        {
          ...taskDefault,
          taskId: 6,
          templateName: 'Dokument scannen',
          taskTemplateId: 1,
          done: true
        },
        {
          ...taskDefault,
          taskId: 7,
          templateName: 'Prüfen (Admin)',
          taskTemplateId: 2
        },
        {
          ...taskDefault,
          taskId: 8,
          templateName: 'Prüfen (Asset Manager)',
          taskTemplateId: 3
        },
        {
          ...taskDefault,
          taskId: 9,
          templateName: 'Zahlung veranlassen',
          taskTemplateId: 4
        },
        {
          ...taskDefault,
          taskId: 10,
          templateName: 'Buchung eintragen',
          taskTemplateId: 5
        }
      ]
    }
    return Promise.resolve([proc1, proc2, proc3])
  }
}

export default ProcessApi
