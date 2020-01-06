// @flow

import React from 'react'
import { H2 } from '@blueprintjs/core'
import type { ProcessTemplateType } from '../../../modules/datatypes/Process'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import ProcessApi from '../../../modules/api/ProcessApi'
import type { UserType } from '../../../modules/datatypes/User'
import UsersApi from '../../../modules/api/UsersApi'
import ProcessTemplateCard from './ProcessTemplateCard'

type PropsType = { templates: ProcessTemplateType[], users: Map<string, UserType> }

class ProcessTemplates extends React.Component<PropsType> {
  render () {
    const { users, templates } = this.props
    return <div style={{
      display: 'flex',
      flexDirection: 'column'
    }}>
      <H2 style={{ textAlign: 'center' }}>All Process Templates</H2>
      <div style={{
        margin: '10px 20px',
        display: 'flex',
        flex: '1',
        justifyContent: 'flex-start',
        alignItems: 'stretch',
        flexDirection: 'row'
      }}>
        {templates.map(template => <ProcessTemplateCard key={template.id} template={template} users={users}/>)}
      </div>
    </div>
  }
}

export default withPromiseResolver<PropsType, PropsType>(
  () => Promise.all([
    new ProcessApi().getAllProcessTemplates()
      .then(templates => new ProcessApi().getProcessTemplates(templates
        .filter(template => !template.deleted)
        .map(template => template.id))
      ),
    new UsersApi().getUsers()
  ]).then(([templates, users]) => {
    return ({
      templates,
      users
    })
  })
)(ProcessTemplates)
