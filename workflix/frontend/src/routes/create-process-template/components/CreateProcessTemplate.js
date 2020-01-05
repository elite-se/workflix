// @flow

import React from 'react'
import type { IncompleteProcessTemplateType } from '../../../modules/process-template-editor/components/ProcessTemplateEditor'
import ProcessTemplateEditor from '../../../modules/process-template-editor/components/ProcessTemplateEditor'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import type { ProcessTemplateType } from '../../../modules/datatypes/Process'
import type { FilledProcessTemplateType } from '../../../modules/api/ProcessApi'
import ProcessApi from '../../../modules/api/ProcessApi'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import UserApi from '../../../modules/api/UsersApi'
import { navigate } from '@reach/router'

type PropsType = {
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>,
  initialProcessTemplate: ?ProcessTemplateType
}

class CreateProcessTemplate extends React.Component<PropsType> {
  onSave = async (processTemplate: FilledProcessTemplateType) => {
    await new ProcessApi().addProcessTemplate(processTemplate)
    navigate('/process-templates')
  }

  render () {
    const { users, userRoles } = this.props
    const initialProcessTemplate: IncompleteProcessTemplateType = {
      tasks: [],
      title: '',
      description: '',
      durationLimit: null,
      owner: null
    }
    return <ProcessTemplateEditor userRoles={userRoles} users={users} initialProcessTemplate={initialProcessTemplate}
                                  title='Create Process Template' onSave={this.onSave}/>
  }
}

const promiseCreator = () => Promise.all([
  new UserApi().getUsers(),
  new UserApi().getUserRoles()
]).then(([users, userRoles]) => ({
  users,
  userRoles
}))

export default withPromiseResolver<*, *>(promiseCreator)(CreateProcessTemplate)
