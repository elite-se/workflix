// @flow

export type TaskType = {
  taskId: number,
  templateName: string,
  templateDescription: string,
  taskTemplateId: number,
  simpleClosing: boolean,
  personsResponsible: {
    personResponsibleId: string,
    done: boolean
  }[],
  done: boolean
}
