// @flow

import React from 'react'
import type { IncompleteProcessTemplateType } from '../../../modules/process-template-editor/components/ProcessTemplateEditor'
import ProcessTemplateEditor from '../../../modules/process-template-editor/components/ProcessTemplateEditor'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import type { ProcessTemplateType } from '../../../modules/datatypes/Process'
import UserApi from '../../../modules/api/UsersApi'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import type { FilledProcessTemplateType } from '../../../modules/api/ProcessApi'
import ProcessApi from '../../../modules/api/ProcessApi'

type PropsType = {
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>,
  initialProcessTemplate: ProcessTemplateType
}

class EditProcessTemplate extends React.Component<PropsType> {
  onSave = (processTemplate: FilledProcessTemplateType) => {
    // todo: Save edited template
  }

  render () {
    const { users, userRoles, initialProcessTemplate } = this.props
    const initial: IncompleteProcessTemplateType = {
      tasks: initialProcessTemplate.taskTemplates.map(task => ({ ...task })),
      durationLimit: initialProcessTemplate.durationLimit,
      description: initialProcessTemplate.description,
      title: initialProcessTemplate.title,
      owner: users.get(initialProcessTemplate.ownerId)
    }
    return <ProcessTemplateEditor userRoles={userRoles} users={users} title='Edit Process Template'
                                  initialProcessTemplate={initial} onSave={this.onSave}/>
  }
}

const promiseCreator = ({ id }: { id: number }) => Promise.all([
  new UserApi().getUsers(),
  new UserApi().getUserRoles(),
  new ProcessApi().getProcessTemplate(id)
]).then(([users, userRoles, initialProcessTemplate]) => ({
  users,
  userRoles,
  initialProcessTemplate
}))

export default withPromiseResolver<*, *>(promiseCreator)(EditProcessTemplate)
