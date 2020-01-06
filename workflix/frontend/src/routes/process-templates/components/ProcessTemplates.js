// @flow

import React from 'react'
import { H2 } from '@blueprintjs/core'
import type { ProcessTemplateType } from '../../../modules/datatypes/Process'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import ProcessApi from '../../../modules/api/ProcessApi'
import type { UserType } from '../../../modules/datatypes/User'
import UsersApi from '../../../modules/api/UsersApi'
import ProcessTemplateCard from './ProcessTemplateCard'
import styled from 'styled-components'
import FlipMove from 'react-flip-move'

const TemplatesContainer = styled<{}, {}, 'div'>(FlipMove)`
  margin: 10px 20px;
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: start;
  flex-direction: row;
  max-width: 100%;
  flex-wrap: wrap;
`

type PropsType = { templates: ProcessTemplateType[], users: Map<string, UserType>, refresh: (soft: boolean) => void }

class ProcessTemplates extends React.Component<PropsType> {
  refresh = () => this.props.refresh(true)

  render () {
    const { users, templates } = this.props
    return <div style={{
      display: 'flex',
      flexDirection: 'column'
    }}>
      <H2 style={{ textAlign: 'center' }}>All Process Templates</H2>
      <TemplatesContainer>
        {templates.map(template => <ProcessTemplateCard key={template.id} template={template} users={users}
                                                        refresh={this.refresh}/>)}
      </TemplatesContainer>
    </div>
  }
}

export default withPromiseResolver<PropsType, PropsType>(
  (props, refresh) => Promise.all([
    new ProcessApi().getAllProcessTemplates()
      .then(templates => new ProcessApi().getProcessTemplates(templates
        .filter(template => !template.deleted)
        .map(template => template.id))
      ),
    new UsersApi().getUsers()
  ]).then(([templates, users]) => {
    return ({
      templates,
      users,
      refresh
    })
  })
)(ProcessTemplates)
