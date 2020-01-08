// @flow

export const parseDatesInProcess = (process: any) => ({
  ...process,
  startedAt: process.startedAt && new Date(process.startedAt),
  tasks: process.tasks.map(parseDatesInTask)
})

export const parseDatesInTask = (task: any) => ({
  ...task,
  comments: task.comments.map(parseDatesInComment),
  assignments: task.assignments.map(parseDatesInAssignments)
})

export const parseDatesInComment = (comment: any) => ({
  ...comment,
  createdAt: comment.createdAt && new Date(comment.createdAt)
})

export const parseDatesInAssignments = (ass: any) => ({
  ...ass,
  createdAt: ass.createdAt && new Date(ass.createdAt),
  doneAt: ass.doneAt && new Date(ass.doneAt)
})

export const parseDatesInProcessTemplate = (template: any) => ({
  ...template,
  createdAt: template.createdAt && new Date(template.createdAt)
})

export const parseDatesInUserRole = (role: any) => ({
  ...role,
  createdAt: role.createdAt && new Date(role.createdAt)
})

export const parseDatesInProcessGroup = (group: any) => ({
  ...group,
  createdAt: group.createdAt && new Date(group.createdAt)
})
