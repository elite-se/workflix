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
      templateDescription: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor ' +
        'invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo ' +
        'dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ' +
        'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore ' +
        'et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. ' +
        'Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',
      comments: [],
      assignments: [],
      status: 'RUNNING'
    }
    const commentDefault1 = {
      creatorId: 'ek24021998',
      title: 'I bims 1 Comment',
      content: 'I bims 1 krasse comment description vong mockigkeit her.\n' +
        'Tun auch Emoji wie 👨‍💻 funktionieren tun?\n' +
        ' 🎄🎅  Happy christmas!',
      createdAt: '24-12-2019 20:15:00',
      doneAt: undefined
    }
    const commentDefault2 = {
      creatorId: 'hw123456',
      title: 'I bims 1 anderer Comment',
      content: 'Hier steht nur unwichter Test-text. Bitte ignorieren! Donaudampfschifffahrtskapitänsmützenschirm.',
      createdAt: '24-12-2019 20:16:00',
      doneAt: undefined
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
          status: 'CLOSED',
          assignments: [{
            assigneeId: 'mm12345678',
            status: 'CLOSED',
            createdAt: '12-04-2019 13:44:12',
            doneAt: undefined
          }],
          comments: [{
            ...commentDefault1,
            id: 201
          }, {
            ...commentDefault2,
            id: 202
          }]
        },
        {
          ...taskDefault,
          taskId: 2,
          templateName: 'Prüfen (Admin)',
          taskTemplateId: 2,
          status: 'CLOSED'
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
          status: 'CLOSED'
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
          status: 'CLOSED'
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
